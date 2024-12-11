package tomocomd.subsetsearch;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tomocomd.configuration.dcs.AAttributeDCS;
import tomocomd.configuration.dcs.DCSFactory;
import tomocomd.configuration.dcs.HeadFactory;
import tomocomd.configuration.subsetsearch.AexopConfig;
import tomocomd.data.PopulationInstances;
import tomocomd.descriptors.AttributeComputerFactory;
import tomocomd.exceptions.AExOpDCSException;
import tomocomd.io.CSVManage;
import tomocomd.subsetsearch.evaluation.subsetevaluation.EvaluateSubsetFactory;
import tomocomd.subsetsearch.evaluation.subsetevaluation.IEvaluateSubset;
import tomocomd.subsetsearch.replace.resetpoblation.ResetPopulation;
import tomocomd.utils.ResourceMetrics;

@Getter
public class AexopDcs {

  private static final Logger LOGGER = LogManager.getLogger(AexopDcs.class);

  private static final double FITNESS_TOLERANCE = 0.0001;
  private static final int MAX_ITERATIONS_WITH_LITTLE_CHANGES = 10;
  private static final int MAX_ITERATIONS_WITHOUT_CHANGES = 10;

  private static final String NAME_STATUS = "iterations";
  private static final String STARTING = "Starting iteration: {}";
  private static final String MSS_ERROR_COMPUTED_SPLIT_DESC =
      "Error splitting computed molecular descriptors set";

  private int curIter;
  private int iterationsWithoutChanges;
  private int iterationsWithLittleChanges;
  private final AexopConfig conf;

  private final List<DCSEvolutive> populations;
  private PopulationInstances[] coopsPob;
  private PopulationInstances bestSubset;

  private final String pathOut;

  private final PopulationInstances targetInstances;
  private final ResourceMetrics resourceMetrics;

  public AexopDcs(
      AexopConfig conf,
      String outFile,
      String inputFile,
      PopulationInstances target,
      AttributeComputerFactory attributeComputerFactory,
      HeadFactory headFactory,
      DCSFactory dcsFactory)
      throws AExOpDCSException {

    pathOut = outFile;

    targetInstances = new PopulationInstances(target);
    targetInstances.setClassIndex(0);

    curIter = 0;
    this.conf = conf;
    iterationsWithoutChanges = 0;
    iterationsWithLittleChanges = 0;
    populations = new LinkedList<>();

    for (AAttributeDCS head : conf.getAAttributeDCSList()) {
      populations.add(
          new DCSEvolutive(
              conf.getDcsEvolutiveConfig(),
              head,
              inputFile,
              attributeComputerFactory,
              headFactory,
              dcsFactory));
      populations.get(populations.size() - 1).resetBestSubset(getTargetInstances());
    }
    if (Boolean.TRUE.equals(conf.getCoop())) coopsPob = new PopulationInstances[populations.size()];
    LOGGER.info("AexopDcs objected created");
    resourceMetrics = new ResourceMetrics();
  }

  public void compute() throws AExOpDCSException {
    LOGGER.info("Starting subset search with configuration {}", conf);
    long startTime = System.currentTimeMillis();
    for (curIter = 0; curIter < conf.getNumIter(); curIter++) {
      LOGGER.info(STARTING, curIter + 1);
      LOGGER.debug(STARTING, curIter + 1);
      long startTimeIter = System.currentTimeMillis();
      executeIter();
      LOGGER.info(
          "Completed iteration: {} in {} ms",
          curIter + 1,
          System.currentTimeMillis() - startTimeIter);
      LOGGER.debug("Completed iteration: {}", curIter + 1);
    }
    LOGGER.info(
        "Best subset found with {} molecular descriptors and fitness={}",
        bestSubset.numAttributes(),
        bestSubset.getEvaSub());
    LOGGER.info("Subset search completed in {} ms", System.currentTimeMillis() - startTime);
  }

  /**
   * Executes a single iteration of the algorithm, including population generation, cooperative
   * learning, and molecular descriptor evaluation.
   *
   * @throws AExOpDCSException if there's an issue executing the iteration.
   */
  private void executeIter() throws AExOpDCSException {
    long initOpe = System.currentTimeMillis();
    generatePopulation();
    LOGGER.info(
        "Step 1: Population generation take {} milliseconds", System.currentTimeMillis() - initOpe);

    initOpe = System.currentTimeMillis();
    executeGetInstances4Coop();
    LOGGER.info(
        "Step 2.1: Generate individuals for cooperative step take {} milliseconds",
        System.currentTimeMillis() - initOpe);

    initOpe = System.currentTimeMillis();
    resourceMetrics.logMetrics("executeComputeMDQuality(before)", getClass().getName());
    executeComputeMDQuality();
    resourceMetrics.logMetrics("executeComputeMDQuality(after)", getClass().getName());
    LOGGER.info(
        "Step 2.2: Computed MD quality take {} milliseconds", System.currentTimeMillis() - initOpe);

    PopulationInstances totalPop = populations.get(0).getBestSubset();
    for (int i = 1; i < populations.size(); i++)
      totalPop = PopulationInstances.merge(totalPop, populations.get(i).getBestSubset());

    initOpe = System.currentTimeMillis();
    resourceMetrics.logMetrics("updateBestSubset(before)", getClass().getName());
    boolean resetCauseLocal = updateBestSubset(totalPop);
    resourceMetrics.logMetrics("updateBestSubset(after)", getClass().getName());
    LOGGER.info(
        "Step 3: Get best subset take {} milliseconds", System.currentTimeMillis() - initOpe);
    initOpe = System.currentTimeMillis();
    if (getResetCondition(curIter + 1) || resetCauseLocal) {
      populations.forEach(population -> population.resetBestSubset(getTargetInstances()));
      LOGGER.info(
          "Step 4: Reset population take {} milliseconds", System.currentTimeMillis() - initOpe);
    } else {
      executeEvolutionSteps();
    }
    updateExecStatus();
  }

  /**
   * Generates the initial population based on headings.
   *
   * @throws AExOpDCSException if there's an issue generating the population.
   */
  public void generatePopulation() throws AExOpDCSException {
    resourceMetrics.logMetrics("generatePopulation(before)", getClass().getName());
    try {
      populations.parallelStream().forEach(DCSEvolutive::generatePopulation);
    } catch (Exception e) {
      throw AExOpDCSException.ExceptionType.AEXOPDCS_EXCEPTION.get(
          "Error generating new population", e);
    }
    resourceMetrics.logMetrics("generatePopulation(after)", getClass().getName());
  }

  /**
   * Retrieves instances for cooperative learning, if enabled and not in reset condition.
   *
   * @throws AExOpDCSException if there's an issue retrieving instances.
   */
  private void executeGetInstances4Coop() throws AExOpDCSException {
    if (Boolean.TRUE.equals(conf.getCoop()) && !getResetCondition(curIter)) getInstances4Coop();
  }

  /**
   * Determines if the population should be reset based on the given iteration and GA reset
   * configuration.
   *
   * @param iter the current iteration number.
   * @return true if the population should be reset, false otherwise.
   */
  private boolean getResetCondition(int iter) {
    ResetPopulation gaResetPopulation = new ResetPopulation(conf.getGaResetConf(), iter);
    return gaResetPopulation.resetPopulation();
  }

  /**
   * Retrieves instances for cooperative learning by merging the selected individuals from each
   * population.
   *
   * @throws AExOpDCSException if there's an issue retrieving instances.
   */
  protected void getInstances4Coop() throws AExOpDCSException {
    resourceMetrics.logMetrics("executeGetInstances4Coop(before)", getClass().getName());
    coopsPob = new PopulationInstances[populations.size()];
    List<PopulationInstances> populationsForCoop = new ArrayList<>(populations.size());

    try {
      for (DCSEvolutive population : populations) {
        populationsForCoop.add(population.selectionK4Cooperative());
      }

      mergePopulations(populationsForCoop);
    } catch (Exception e) {
      throw AExOpDCSException.ExceptionType.AEXOPDCS_EXCEPTION.get(e);
    }
    resourceMetrics.logMetrics("executeGetInstances4Coop(before)", getClass().getName());
  }

  /**
   * Merges the selected individuals from each population into a single cooperative population.
   *
   * @param populationsForCoop the selected individuals from each population.
   */
  private void mergePopulations(List<PopulationInstances> populationsForCoop) {
    for (int i = 0; i < populationsForCoop.size(); i++) {
      for (int j = 0; j < populationsForCoop.size(); j++) {
        if (i != j) {
          coopsPob[i] = PopulationInstances.merge(coopsPob[i], populationsForCoop.get(j));
        }
      }
    }
  }

  /**
   * Evaluates the molecular descriptors for each population in parallel.
   *
   * @throws AExOpDCSException if there's an issue evaluating molecular descriptors.
   */
  void executeComputeMDQuality() throws AExOpDCSException {
    int numThreads = Math.min(Runtime.getRuntime().availableProcessors(), populations.size());
    ExecutorService executor = Executors.newFixedThreadPool(numThreads);
    List<CompletableFuture<Void>> futures =
        IntStream.range(0, populations.size())
            .mapToObj(
                idxPop ->
                    CompletableFuture.runAsync(
                        () -> populations.get(idxPop).evaluateMd(coopsPob[idxPop]), executor))
            .collect(Collectors.toList());

    try {
      CompletableFuture<Void> allOf =
          CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
      allOf.get(); // Wait for all tasks to complete
    } catch (InterruptedException | ExecutionException e) {
      Thread.currentThread().interrupt();
      throw AExOpDCSException.ExceptionType.AEXOPDCS_EXCEPTION.get(
          "Error evaluating molecular descriptors", e.getCause());
    } finally {
      executor.shutdown(); // Shutdown the executor
      try {
        if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
          executor.shutdownNow(); // Force shutdown if tasks take too long
        }
      } catch (InterruptedException e) {
        executor.shutdownNow();
        Thread.currentThread().interrupt();
      }
    }
  }

  /**
   * Updates the best subset if the new subset is better.
   *
   * @param subIter the new subset to compare with the current best subset
   * @return true if the population is reset, false otherwise
   * @throws AExOpDCSException if an error occurs during the comparison
   */
  boolean updateBestSubset(PopulationInstances subIter) throws AExOpDCSException {
    boolean isSubsetUpdated;
    double lastBest = 0;
    PopulationInstances frontInst = new PopulationInstances(subIter);
    if (bestSubset == null) {
      isSubsetUpdated = true;
      IEvaluateSubset eva = EvaluateSubsetFactory.getEvaluateSubsetMethod(conf.getSubsetEva());
      bestSubset = eva.bestSubset(subIter);
    } else {
      PopulationInstances oldBestSubset = new PopulationInstances(bestSubset);
      lastBest = bestSubset.getEvaSub();
      // compare with the union
      try {
        PopulationInstances merge = PopulationInstances.merge(subIter, oldBestSubset);
        isSubsetUpdated = compareSubsets(merge);
        if (isSubsetUpdated) {
          frontInst = new PopulationInstances(merge);
        }
      } catch (Exception e) {
        throw AExOpDCSException.ExceptionType.AEXOPDCS_EXCEPTION.get(
            "Error comparing merged subset", e);
      }
    }

    return shouldResetPopulation(isSubsetUpdated, lastBest, frontInst);
  }

  /**
   * Checks if the population should be reset based on the fitness values.
   *
   * @param isSubsetUpdated whether the subset has been updated
   * @param lastBest the last best fitness value
   * @param frontInst the population instances
   * @return true if the population should be reset, false otherwise
   */
  private boolean shouldResetPopulation(
      boolean isSubsetUpdated, double lastBest, PopulationInstances frontInst) {
    if (isSubsetUpdated) {
      saveNewSubset(frontInst);
      if (bestSubset.getEvaSub() - lastBest <= FITNESS_TOLERANCE) {
        iterationsWithLittleChanges++;
        if (iterationsWithLittleChanges == MAX_ITERATIONS_WITH_LITTLE_CHANGES) {
          resetIterationCounts();
          LOGGER.debug("Reset population cause by not changes in fitness values");
          return true;
        }
      } else {
        resetIterationCounts();
      }
    } else {
      iterationsWithoutChanges++;
      if (iterationsWithoutChanges == MAX_ITERATIONS_WITHOUT_CHANGES) {
        resetIterationCounts();
        LOGGER.debug("Reset population cause by local optimum");
        return true;
      }
    }
    return false;
  }

  /** Resets the iteration counts. */
  private void resetIterationCounts() {
    iterationsWithLittleChanges = 0;
    iterationsWithoutChanges = 0;
  }

  /**
   * Compares a new subset with the current best subset and updates the best subset if the new
   * subset is better.
   *
   * @param newSubset the new subset to compare with the current best subset
   * @return true if the best subset is updated, false otherwise
   * @throws AExOpDCSException if an error occurs during the comparison
   */
  private boolean compareSubsets(PopulationInstances newSubset) throws AExOpDCSException {

    IEvaluateSubset eva = EvaluateSubsetFactory.getEvaluateSubsetMethod(conf.getSubsetEva());
    PopulationInstances newBest = eva.bestSubset(newSubset);

    if (newBest.numAttributes() == 1) return false;

    if (newBest.getEvaSub() > bestSubset.getEvaSub()
        || (newBest.getEvaSub() == bestSubset.getEvaSub()
            && newBest.numAttributes() < bestSubset.numAttributes())) {
      bestSubset = new PopulationInstances(newBest);
      return true;
    }
    return false;
  }

  private void saveNewSubset(PopulationInstances frontInst) {
    String nameDir = String.format("%s_best%d.csv", pathOut, curIter + 1);
    CSVManage.saveDescriptorMResult(bestSubset, nameDir);
    bestSubset.setRelationName(nameDir);
    String nameDirComplete = String.format("%s_complete%d.csv", pathOut, curIter + 1);
    LOGGER.info(
        "New best subset found with {} molecular descriptors and fitness={}",
        bestSubset.numAttributes(),
        bestSubset.getEvaSub());
    CSVManage.saveDescriptorMResult(frontInst, nameDirComplete);
  }

  protected void executeEvolutionSteps() throws AExOpDCSException {
    resourceMetrics.logMetrics("executeEvolutionSteps(before)", getClass().getName());
    long initOpe = System.currentTimeMillis();
    for (DCSEvolutive dcsEvolutive : populations) {
      dcsEvolutive.executeEvolutiveSteps(curIter);
    }
    LOGGER.info(
        "Step 4: Genetic operators take {} milliseconds", System.currentTimeMillis() - initOpe);
    resourceMetrics.logMetrics("executeEvolutionSteps(after)", getClass().getName());
  }

  private void updateExecStatus() throws AExOpDCSException {
    try (PrintWriter pw = new PrintWriter(NAME_STATUS)) {
      pw.println(curIter + 1);
    } catch (FileNotFoundException e) {
      throw AExOpDCSException.ExceptionType.AEXOPDCS_EXCEPTION.get("Error writing status file", e);
    }
  }
}
