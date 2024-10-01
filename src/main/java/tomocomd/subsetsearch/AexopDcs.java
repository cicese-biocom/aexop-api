package tomocomd.subsetsearch;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tomocomd.configuration.dcs.AAttributeDCS;
import tomocomd.configuration.dcs.AHeadEntity;
import tomocomd.configuration.subsetsearch.AexopConfig;
import tomocomd.data.PopulationInstances;
import tomocomd.descriptors.StartpepDescriptorExecutor;
import tomocomd.exceptions.AExOpDCSException;
import tomocomd.io.CSVManage;
import tomocomd.subsetsearch.evaluation.subsetevaluation.EvaluateSubsetFactory;
import tomocomd.subsetsearch.evaluation.subsetevaluation.IEvaluateSubset;
import tomocomd.subsetsearch.replace.resetpoblation.ResetPopulation;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

@Getter
public class AexopDcs {

  private static final Logger LOGGER = LogManager.getLogger(AexopDcs.class);

  private static final double FITNESS_TOLERANCE = 0.001;
  private static final int MAX_ITERATIONS_WITH_LITTLE_CHANGES = 3;
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

  private final String fastaFile;
  private final String pathOut;

  private final PopulationInstances targetInstances;

  public AexopDcs(AexopConfig conf, String outFile, String fastaFile, String pathCsvTarget)
      throws AExOpDCSException {

    this.fastaFile = fastaFile;
    pathOut = outFile;

    targetInstances = new PopulationInstances(CSVManage.loadCSV(pathCsvTarget));
    targetInstances.setClassIndex(0);

    curIter = 0;
    this.conf = conf;
    iterationsWithoutChanges = 0;
    iterationsWithLittleChanges = 0;
    populations = new LinkedList<>();

    for (AAttributeDCS head : conf.getAAttributeDCSList()) {
      populations.add(new DCSEvolutive(conf.getDcsEvolutiveConfig(), head));
      populations.get(populations.size() - 1).resetBestSubset(getTargetInstances());
    }
    if (Boolean.TRUE.equals(conf.getCoop())) coopsPob = new PopulationInstances[populations.size()];
    LOGGER.info("AexopDcs objected created");
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
        "Step 2.1: Generate individuos for cooperative step take {} milliseconds",
        System.currentTimeMillis() - initOpe);

    initOpe = System.currentTimeMillis();
    executeComputeMDQuality();
    LOGGER.info(
        "Step 2.2: Computed MD quality take {} milliseconds", System.currentTimeMillis() - initOpe);

    PopulationInstances totalPop = populations.get(0).getBestSubset();
    for (int i = 1; i < populations.size(); i++)
      totalPop = PopulationInstances.merge(totalPop, populations.get(i).getBestSubset());

    initOpe = System.currentTimeMillis();
    boolean resetCauseLocal = updateBestSubset(totalPop);
    LOGGER.info(
        "Step 3: Get best subset take {} milliseconds", System.currentTimeMillis() - initOpe);
    initOpe = System.currentTimeMillis();
    if (getResetCondition(curIter + 1) || resetCauseLocal) {
      populations.forEach(population -> population.resetBestSubset(getTargetInstances()));
      LOGGER.info(
          "Step 4: Reset population take {} milliseconds", System.currentTimeMillis() - initOpe);
    } else {
      initOpe = System.currentTimeMillis();
      Map<Integer, PopulationInstances> newPop = executeEvolutiveSteps();
      LOGGER.info(
          "Step 4: Genetic operators take {} milliseconds", System.currentTimeMillis() - initOpe);
      initOpe = System.currentTimeMillis();
      executeMDReplace(newPop);
      LOGGER.info("Step 5: MD replace take {} milliseconds", System.currentTimeMillis() - initOpe);
    }
    updateExecStatus();
  }

  /**
   * Generates the initial population based on headings.
   *
   * @throws AExOpDCSException if there's an issue generating the population.
   */
  public void generatePopulation() throws AExOpDCSException {
    Map<Integer, Set<String>> headingsByPopulation = new LinkedHashMap<>();
    Set<String> allGeneratedHeads = generateHeadings(headingsByPopulation);

    while (!allGeneratedHeads.isEmpty()) {
      PopulationInstances instances = computeDesc(allGeneratedHeads);
      Map<Integer, PopulationInstances> data4Pop =
          splitTotalPopulation(instances, headingsByPopulation);
      processPopulationData(data4Pop);

      allGeneratedHeads = generateHeadings(headingsByPopulation);
    }
  }

  /**
   * Processes the population data by merging subsets and applying filters.
   *
   * @param populationData the population data to process.
   */
  private void processPopulationData(Map<Integer, PopulationInstances> populationData) {
    for (Map.Entry<Integer, PopulationInstances> entry : populationData.entrySet()) {
      if (!entry.getValue().isEmpty()) {
        populations.get(entry.getKey()).mergeSubset(entry.getValue());
        populations.get(entry.getKey()).applyFilter();
      }
    }
  }

  /**
   * Generates headings for each population and stores them in the population headings map.
   *
   * @param populationHeadings the map to store population headings.
   * @return a set of all headings.
   * @throws AExOpDCSException if there's an issue generating headings.
   */
  private Set<String> generateHeadings(Map<Integer, Set<String>> populationHeadings)
      throws AExOpDCSException {
    Set<String> allHeadings = new LinkedHashSet<>();

    for (int i = 0; i < populations.size(); i++) {
      int numDescriptors =
          populations.get(i).getNumDesc() - populations.get(i).populationSize() + 1;
      Set<String> headings =
          populations.get(i).generateHeadings(numDescriptors).stream()
              .map(AHeadEntity::toString)
              .collect(Collectors.toSet());
      populationHeadings.put(i, headings);
      allHeadings.addAll(headings);
    }
    return allHeadings;
  }

  /**
   * Computes molecular descriptors for the given set of headings.
   *
   * @param allHeads the set of headings to compute descriptors for.
   * @return the computed molecular descriptors.
   * @throws AExOpDCSException if there's an issue computing descriptors.
   */
  private PopulationInstances computeDesc(Set<String> allHeads) throws AExOpDCSException {

    StartpepDescriptorExecutor molecularDescriptorCalculator = new StartpepDescriptorExecutor();
    long initTime = System.currentTimeMillis();
    PopulationInstances data = molecularDescriptorCalculator.compute(allHeads, fastaFile);
    LOGGER.debug(
        "Computed {} molecular descriptors take {} ms",
        allHeads.size(),
        System.currentTimeMillis() - initTime);
    return data;
  }

  /**
   * Splits the total population into sub-populations based on the given set of population headings.
   *
   * @param data the total population to split.
   * @param populationHeadings the map of population headings to attribute names.
   * @return the map of population IDs to sub-populations.
   * @throws AExOpDCSException if there's an issue splitting the population.
   */
  private Map<Integer, PopulationInstances> splitTotalPopulation(
      PopulationInstances data, Map<Integer, Set<String>> populationHeadings)
      throws AExOpDCSException {

    Map<Integer, Set<Integer>> populationIndexMap = new LinkedHashMap<>();
    // Initialize population index map with class index, if applicable
    int classIndex = data.classIndex();
    if (classIndex >= 0) {
      populationHeadings
          .keySet()
          .forEach(
              idxPop ->
                  populationIndexMap.put(
                      idxPop, new LinkedHashSet<>(Collections.singletonList(data.classIndex()))));
    }

    IntStream.range(0, data.numAttributes())
        .forEach(
            idxAtt -> {
              String name = data.attribute(idxAtt).name();
              populationHeadings.forEach(
                  (idPop, heads) -> {
                    if (heads.contains(name)) {
                      populationIndexMap
                          .computeIfAbsent(idPop, k -> new LinkedHashSet<>())
                          .add(idxAtt);
                    }
                  });
            });

    Map<Integer, PopulationInstances> subPopulations = new LinkedHashMap<>();
    for (Map.Entry<Integer, Set<Integer>> entry : populationIndexMap.entrySet()) {
      int[] pos = entry.getValue().stream().mapToInt(Integer::intValue).toArray();
      Remove remove = new Remove();
      remove.setAttributeIndicesArray(pos);
      remove.setInvertSelection(true);
      try {
        remove.setInputFormat(new PopulationInstances(data));
        subPopulations.put(
            entry.getKey(),
            new PopulationInstances(Filter.useFilter(new PopulationInstances(data), remove)));
      } catch (Exception e) {
        throw AExOpDCSException.ExceptionType.DCS_EVOLUTION_EXCEPTION.get(
            MSS_ERROR_COMPUTED_SPLIT_DESC, e);
      }
    }
    return subPopulations;
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
    List<CompletableFuture<Void>> futures =
        IntStream.range(0, populations.size())
            .mapToObj(
                idxPop ->
                    CompletableFuture.runAsync(
                        () -> populations.get(idxPop).evaluateMd(coopsPob[idxPop])))
            .collect(Collectors.toList());

    try {
      CompletableFuture<Void> allOf =
          CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
      allOf.get(); // Wait for all tasks to complete
    } catch (InterruptedException | ExecutionException e) {
      Thread.currentThread().interrupt();
      throw AExOpDCSException.ExceptionType.AEXOPDCS_EXCEPTION.get(
          "Error evaluating molecular descriptors", e.getCause());
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

  /**
   * Executes the MD replace operation for each population in parallel.
   *
   * @param children4Population a map containing the population index and corresponding instances
   * @throws AExOpDCSException if an error occurs during the replace operation
   */
  protected void executeMDReplace(Map<Integer, PopulationInstances> children4Population)
      throws AExOpDCSException {

    List<CompletableFuture<Void>> futures =
        children4Population.entrySet().stream()
            .map(
                entry ->
                    CompletableFuture.runAsync(
                        () -> populations.get(entry.getKey()).makeReplace(entry.getValue())))
            .collect(Collectors.toList());

    try {
      CompletableFuture<Void> allOf =
          CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
      allOf.get(); // Wait for all tasks to complete
    } catch (InterruptedException | ExecutionException e) {
      Thread.currentThread().interrupt();
      throw AExOpDCSException.ExceptionType.AEXOPDCS_EXCEPTION.get(
          "Error Executing replace operators", e.getCause());
    }
  }

  protected Map<Integer, PopulationInstances> executeEvolutiveSteps() throws AExOpDCSException {
    int sumReal = 0;
    Map<Integer, PopulationInstances> children4Population = null;
    int sumExp =
        conf.getDcsEvolutiveConfig().getSelConf().getCant() * conf.getAAttributeDCSList().size();
    int numRep = 1;
    while (sumReal < sumExp) {

      // get size for population
      long initTime = System.currentTimeMillis();
      Map<Integer, Integer> size4Pop = getSize4Pop(children4Population);

      LOGGER.info(
          "Step 4.1: Compute population size take: {} ms in iteration {}",
          System.currentTimeMillis() - initTime,
          numRep);

      // genetic operator for family
      initTime = System.currentTimeMillis();

      executeGeneticOperators(size4Pop);
      LOGGER.info(
          "Step 4.2: Genetic operators take {} ms in iteration {}",
          System.currentTimeMillis() - initTime,
          numRep);

      // compute
      initTime = System.currentTimeMillis();
      Map<Integer, PopulationInstances> children4PopulationLocal =
          getChildrenAndCompute(size4Pop.keySet());
      LOGGER.info(
          "Step 4.3: Compute new MD children take {} ms in iteration {}",
          System.currentTimeMillis() - initTime,
          numRep);

      // filter
      initTime = System.currentTimeMillis();
      children4PopulationLocal = applyFilter2Children(children4PopulationLocal);
      LOGGER.info(
          "Step 4.4: Apply MD filter take {} ms in iteration {}",
          System.currentTimeMillis() - initTime,
          numRep);

      // merge
      initTime = System.currentTimeMillis();
      if (children4Population == null)
        children4Population = new LinkedHashMap<>(children4PopulationLocal);
      else {
        for (Map.Entry<Integer, PopulationInstances> entry : children4PopulationLocal.entrySet()) {
          children4Population.put(
              entry.getKey(),
              PopulationInstances.merge(children4Population.get(entry.getKey()), entry.getValue()));
        }
      }
      LOGGER.info(
          "Step 4.5: merge new children take {} ms in iteration {}",
          System.currentTimeMillis() - initTime,
          numRep++);

      sumReal =
          children4Population.values().stream().mapToInt(PopulationInstances::numAttributes).sum();
    }
    return children4Population;
  }

  private Map<Integer, Integer> getSize4Pop(Map<Integer, PopulationInstances> children4Population) {
    Map<Integer, Integer> size4Pop = new LinkedHashMap<>();

    int defaultCount = conf.getDcsEvolutiveConfig().getSelConf().getCant();

    IntStream.range(0, populations.size())
        .forEach(
            idx -> {
              int toGen = defaultCount;
              if (children4Population != null && children4Population.get(idx) != null)
                toGen -= children4Population.get(idx).numAttributes();
              if (toGen > 0) size4Pop.put(idx, toGen);
            });

    return size4Pop;
  }

  protected void executeGeneticOperators(Map<Integer, Integer> size4Pop) throws AExOpDCSException {

    List<CompletableFuture<Void>> futures =
        size4Pop.entrySet().stream()
            .map(
                entry ->
                    CompletableFuture.runAsync(
                        () -> populations.get(entry.getKey()).geneticsOperators(entry.getValue())))
            .collect(Collectors.toList());

    try {
      CompletableFuture<Void> allOf =
          CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
      allOf.get(); // Wait for all tasks to complete
    } catch (InterruptedException | ExecutionException e) {
      Thread.currentThread().interrupt();
      throw AExOpDCSException.ExceptionType.AEXOPDCS_EXCEPTION.get(
          "Error Executing genetic operators", e.getCause());
    }
  }

  protected Map<Integer, PopulationInstances> getChildrenAndCompute(Set<Integer> idxPop)
      throws AExOpDCSException {
    Map<Integer, Set<String>> popHeads = new LinkedHashMap<>();
    AtomicInteger pos = new AtomicInteger(0);
    Set<String> allHeads =
        idxPop.stream()
            .map(idx -> populations.get(idx).getChildrenAsString())
            .peek(heads -> popHeads.put(pos.getAndIncrement(), heads))
            .flatMap(Set::stream)
            .collect(Collectors.toCollection(LinkedHashSet::new));

    if (allHeads.isEmpty()) {
      LOGGER.error("No molecular descriptors to compute");
      return new LinkedHashMap<>();
    }
    PopulationInstances instances = computeDesc(allHeads);
    return splitTotalPopulation(instances, popHeads);
  }

  /**
   * Applies filters to children population instances asynchronously.
   *
   * @param children4Population a map containing population instances to be filtered
   * @return a map with filtered population instances
   * @throws AExOpDCSException if an error occurs during the filtering process
   */
  protected Map<Integer, PopulationInstances> applyFilter2Children(
      Map<Integer, PopulationInstances> children4Population) throws AExOpDCSException {

    if (Objects.isNull(children4Population)) {
      LOGGER.error("No molecular descriptors to filter");
      return new LinkedHashMap<>();
    }

    if (children4Population.isEmpty()) {
      LOGGER.error("No molecular descriptors to filter");
      return new LinkedHashMap<>();
    }

    Map<Integer, PopulationInstances> children4PopulationFiltered = new ConcurrentHashMap<>();

    List<CompletableFuture<Void>> futures =
        children4Population.entrySet().stream()
            .map(
                entry ->
                    CompletableFuture.runAsync(
                        () -> {
                          PopulationInstances data = entry.getValue();
                          populations.get(entry.getKey()).applyFilter(data);
                          children4PopulationFiltered.put(entry.getKey(), data);
                        }))
            .collect(Collectors.toList());

    try {
      CompletableFuture<Void> allOf =
          CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
      allOf.get(); // Wait for all tasks to complete
    } catch (InterruptedException | ExecutionException e) {
      Thread.currentThread().interrupt();
      throw AExOpDCSException.ExceptionType.AEXOPDCS_EXCEPTION.get(
          "Error Executing filters operators", e.getCause());
    }

    return children4PopulationFiltered;
  }

  private void updateExecStatus() throws AExOpDCSException {
    try (PrintWriter pw = new PrintWriter(NAME_STATUS)) {
      pw.println(curIter + 1);
    } catch (FileNotFoundException e) {
      throw AExOpDCSException.ExceptionType.AEXOPDCS_EXCEPTION.get("Error writing status file", e);
    }
  }
}
