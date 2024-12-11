package tomocomd.subsetsearch;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tomocomd.configuration.dcs.AAttributeDCS;
import tomocomd.configuration.dcs.AHeadEntity;
import tomocomd.configuration.dcs.DCSFactory;
import tomocomd.configuration.dcs.HeadFactory;
import tomocomd.configuration.subsetsearch.DCSEvolutiveConfig;
import tomocomd.configuration.subsetsearch.operators.GASelectionType;
import tomocomd.data.PopulationInstances;
import tomocomd.descriptors.AttributeComputerFactory;
import tomocomd.descriptors.IAttributeComputer;
import tomocomd.exceptions.AExOpDCSException;
import tomocomd.subsetsearch.evaluation.attributeevaluation.AAtributeEvaluation;
import tomocomd.subsetsearch.evaluation.attributeevaluation.AttributeEvaluationFactory;
import tomocomd.subsetsearch.evolutive.crossover.AGACrossoverOperation;
import tomocomd.subsetsearch.evolutive.crossover.GACrossoverFactory;
import tomocomd.subsetsearch.evolutive.mutation.AGAMutation;
import tomocomd.subsetsearch.evolutive.mutation.GAMutationFactory;
import tomocomd.subsetsearch.evolutive.selection.AbstractGASelectionOperator;
import tomocomd.subsetsearch.evolutive.selection.GASelectionFactory;
import tomocomd.subsetsearch.filters.AbstractMDFilter;
import tomocomd.subsetsearch.filters.FilterFactory;
import tomocomd.subsetsearch.replace.replacesubpopulation.IMDGAReplace;
import tomocomd.subsetsearch.replace.replacesubpopulation.MDGAReplaceFactory;
import tomocomd.utils.Statistics;

/**
 * @author Potter
 */
public final class DCSEvolutive {
  static final Logger LOGGER = LogManager.getLogger(DCSEvolutive.class);

  private static final String FOLDER_NAME_MD = "generated_descriptors";
  private static final String MSG_SEL = "Selected {} parent for {} family";
  // variables execution
  private final String nameGeneratedFile; // file with the generated heads
  private final DCSEvolutiveConfig dcsEvolutiveConfig;
  private final AAttributeDCS attributeDCS;

  private List<Integer> parents;
  private Set<AHeadEntity> children;
  private final AbstractGASelectionOperator selectionOperator;
  //  subsets
  @Getter private PopulationInstances bestSubset;
  private final List<String> generatedHeads;
  private final String inputObjectFile;

  private final AttributeComputerFactory attributeComputerFactory;
  private final HeadFactory headFactory;
  private final DCSFactory dcsFactory;

  public DCSEvolutive(
      DCSEvolutiveConfig gaAlgorithm4PobConf,
      AAttributeDCS attributeDCS,
      String inputObjectFile,
      AttributeComputerFactory attributeComputerFactory,
      HeadFactory headFactory,
      DCSFactory dcsFactory) {

    this.dcsEvolutiveConfig = gaAlgorithm4PobConf;
    this.attributeDCS = attributeDCS;
    selectionOperator = GASelectionFactory.selectionCreator(gaAlgorithm4PobConf.getSelConf());
    this.attributeComputerFactory = attributeComputerFactory;
    this.headFactory = headFactory;
    this.dcsFactory = dcsFactory;

    File folder = new File(FOLDER_NAME_MD);
    if (!folder.exists()) {
      boolean created = folder.mkdir();
      if (!created) LOGGER.warn("Folder {} not created", FOLDER_NAME_MD);
    }
    nameGeneratedFile =
        new File(
                new File(FOLDER_NAME_MD),
                String.format("%s_%d.txt", attributeDCS.getName(), System.currentTimeMillis()))
            .getAbsolutePath();
    generatedHeads = new LinkedList<>();
    this.inputObjectFile = inputObjectFile;
    LOGGER.info(
        "DCS objected for type {} with name {} created",
        attributeDCS.getType(),
        attributeDCS.getName());
  }

  public void generatePopulation() {
    long start = System.currentTimeMillis();
    int numHeadGenerate = getNumDesc() - populationSize() + 1;
    Set<String> heading =
        generateHeadings(numHeadGenerate).stream()
            .map(AHeadEntity::toString)
            .collect(Collectors.toSet());
    while (!heading.isEmpty()) {
      mergeSubset(computeDesc(heading));
      applyFilter();
      numHeadGenerate = getNumDesc() - populationSize() + 1;
      heading =
          generateHeadings(numHeadGenerate).stream()
              .map(AHeadEntity::toString)
              .collect(Collectors.toSet());
    }
    LOGGER.info(
        "Generated {} MD take: {} ms", attributeDCS.getName(), System.currentTimeMillis() - start);
  }

  /**
   * Computes molecular descriptors for the given set of headings.
   *
   * @param allHeads the set of headings to compute descriptors for.
   * @return the computed molecular descriptors.
   * @throws AExOpDCSException if there's an issue computing descriptors.
   */
  private PopulationInstances computeDesc(Set<String> allHeads) throws AExOpDCSException {
    IAttributeComputer molecularDescriptorCalculator =
        attributeComputerFactory.getComputer(attributeDCS.getType().getComputerType());
    long initTime = System.currentTimeMillis();
    LOGGER.debug(
        "Computing {} {} MD of name {}",
        allHeads.size(),
        attributeDCS.getType(),
        attributeDCS.getName());
    PopulationInstances data = molecularDescriptorCalculator.compute(allHeads, inputObjectFile);
    LOGGER.debug(
        "Computed {} molecular descriptors take {} ms",
        allHeads.size(),
        System.currentTimeMillis() - initTime);
    return data;
  }

  public void executeEvolutiveSteps(int curIter) {
    long start = System.currentTimeMillis();
    int generatedChildren = 0;
    int totalChildren = dcsEvolutiveConfig.getSelConf().getCant();
    int toGene;
    PopulationInstances newChildren = null;
    long initTime;
    int numRep = 1;
    while (generatedChildren < totalChildren) {
      toGene = totalChildren - generatedChildren;
      initTime = System.currentTimeMillis();
      geneticsOperators(toGene);
      LOGGER.info(
          "Step 4.1: Genetic operators for {} family take {} ms in iteration {}.{}",
          attributeDCS.getType(),
          System.currentTimeMillis() - initTime,
          curIter,
          numRep);
      initTime = System.currentTimeMillis();
      if (children.isEmpty())
        System.out.println("Error in genetic operators, no children generated");
      PopulationInstances populationInstancesChildren = computeDesc(getChildrenAsString());
      LOGGER.info(
          "Step 4.2: Compute new MD children for {} family take {} ms in iteration {}.{}",
          attributeDCS.getType(),
          System.currentTimeMillis() - initTime,
          curIter,
          numRep);
      initTime = System.currentTimeMillis();
      applyFilter(populationInstancesChildren);
      LOGGER.info(
          "Step 4.3: Apply MD filter for {} family take {} ms in iteration {}.{}",
          attributeDCS.getType(),
          System.currentTimeMillis() - initTime,
          curIter,
          numRep);
      newChildren = PopulationInstances.merge(newChildren, populationInstancesChildren);
      generatedChildren = newChildren.numAttributes();
    }
    if (Objects.nonNull(newChildren)) {
      initTime = System.currentTimeMillis();
      makeReplace(newChildren);
      LOGGER.info(
          "Step 4.4: Apply replace operation for {} family take {} ms in iteration {}.{}",
          attributeDCS.getType(),
          System.currentTimeMillis() - initTime,
          curIter,
          numRep);
      LOGGER.info(
          "Generated {} new children for {} MD take: {} ms",
          newChildren.numAttributes(),
          attributeDCS.getName(),
          System.currentTimeMillis() - start);
    }
  }

  /**
   * Resets the best subset with the given population instances.
   *
   * @param bestSubset the population instances to set as the best subset.
   * @throws NullPointerException if the `bestSubset` argument is null.
   */
  public void resetBestSubset(PopulationInstances bestSubset) {
    if (Objects.isNull(bestSubset)) {
      this.bestSubset = null;
      LOGGER.warn("Best subset is null");
    } else this.bestSubset = new PopulationInstances(bestSubset);
  }

  /**
   * Returns the number of molecular descriptors.
   *
   * @return the number of molecular descriptors.
   */
  public int getNumDesc() {
    return dcsEvolutiveConfig.getNumDesc();
  }

  /**
   * Returns the size of the population.
   *
   * @return the size of the population.
   */
  public int populationSize() {
    return bestSubset == null ? 0 : bestSubset.numAttributes();
  }

  /**
   * Generates a set of headings.
   *
   * @param cant the number of headings to generate.
   * @return a set of headings.
   * @throws AExOpDCSException if an error occurs while generating the headings.
   */
  public Set<AHeadEntity> generateHeadings(int cant) throws AExOpDCSException {
    Set<AHeadEntity> headings = new LinkedHashSet<>();
    AHeadEntity nSHead;
    for (int i = 0; i < cant; i++) {
      do nSHead = attributeDCS.randomHeading();
      while (generatedHeads.contains(nSHead.toString()));
      generatedHeads.add(nSHead.toString());
      headings.add(nSHead);
    }
    writeFileHead(headings);
    return headings;
  }

  /**
   * Writes a set of headings to a file.
   *
   * @param heads the set of headings to write.
   * @throws AExOpDCSException if an error occurs while writing the headings to the file.
   */
  private void writeFileHead(Set<AHeadEntity> heads) throws AExOpDCSException {
    try (FileWriter fw = new FileWriter(nameGeneratedFile, true);
        BufferedWriter w = new BufferedWriter(fw)) {

      for (AHeadEntity head : heads) {
        w.write(head.toString() + "\n");
      }
    } catch (IOException ex) {
      throw AExOpDCSException.ExceptionType.DCS_EVOLUTION_EXCEPTION.get(
          "Error writing head to file", ex);
    }
  }

  /**
   * Merges the best subset of solutions with a new population.
   *
   * @param data the new population of solutions.
   */
  public void mergeSubset(PopulationInstances data) {
    bestSubset = PopulationInstances.merge(bestSubset, data);
  }

  /**
   * Applies the specified filters to the best subset of solutions.
   *
   * @throws AExOpDCSException if the filters cannot be applied.
   */
  public void applyFilter() throws AExOpDCSException {
    AbstractMDFilter filters = FilterFactory.getFilters(dcsEvolutiveConfig.getFiltersConfig());
    filters.filtering(bestSubset);
  }

  public void applyFilter(PopulationInstances data) throws AExOpDCSException {
    AbstractMDFilter filters = FilterFactory.getFilters(dcsEvolutiveConfig.getFiltersConfig());
    filters.filtering(data);
  }

  /**
   * Selects a subset of instances from the best subset of solutions using the cooperative selection
   * operator.
   *
   * @return a new population of instances with the selected subset.
   * @throws AExOpDCSException if the selection operator cannot be applied.
   */
  public PopulationInstances selectionK4Cooperative() throws AExOpDCSException {

    int numAGen = selectionOperator.getCant();
    if (bestSubset == null || bestSubset.numAttributes() == 1)
      return new PopulationInstances("Empty", new ArrayList<>(), 0);

    selectionOperator.build(bestSubset);

    Set<Integer> selectedIdx = new LinkedHashSet<>();

    try {
      while (selectedIdx.size() < numAGen) {
        selectedIdx.add(selectionOperator.getOneParent());
      }
      PopulationInstances selected = new PopulationInstances(bestSubset);
      selected.setClassIndex(-1);
      selected.deleteAttributesByPos(selectedIdx, true);
      return selected;
    } catch (Exception ex) {
      throw AExOpDCSException.ExceptionType.DCS_EVOLUTION_EXCEPTION.get(
          "Problems getting MDs for cooperative procedure", ex);
    }
  }

  /**
   * Evaluates the molecular descriptors in the best subset of solutions using the specified
   * attribute quality function.
   *
   * @param otherPopulations the population of instances to evaluate.
   * @throws AExOpDCSException if the attribute quality function cannot be built or applied.
   */
  public void evaluateMd(PopulationInstances otherPopulations) throws AExOpDCSException {
    if (selectionOperator.getType() != GASelectionType.RANDOM) {
      try {
        // start cooperative scheme
        LOGGER.debug(
            "Starting cooperative step(if is the case) and fitness computing for {} family",
            attributeDCS.getType());
        Set<Integer> pos;
        AAtributeEvaluation attFnc =
            AttributeEvaluationFactory.getAttributeQuality(dcsEvolutiveConfig.getAttConf());

        long startTimeEva = System.currentTimeMillis();
        otherPopulations = PopulationInstances.merge(bestSubset, otherPopulations);
        int sizeMD = otherPopulations.numAttributes();
        attFnc.buildEvaluator(otherPopulations);
        for (int i = 0; i < otherPopulations.numAttributes(); i++) {
          otherPopulations.setEva4DescPos(i, attFnc.evaluate(i));
        }
        pos = new LinkedHashSet<>();
        for (int i = bestSubset.numAttributes(); i < otherPopulations.numAttributes(); i++) {
          pos.add(i);
        }
        otherPopulations.deleteAttributesByPos(pos, false);
        bestSubset = otherPopulations;
        LOGGER.debug(
            "Fitness molecular function performed in {} ms for {} {} molecular descriptors",
            System.currentTimeMillis() - startTimeEva,
            sizeMD,
            attributeDCS.getType());
      } catch (Exception e) {
        throw AExOpDCSException.ExceptionType.MD_EVALUATION_FUNCTION_EXCEPTION.get(
            "Error deleting instances", e);
      }
    }
  }

  /**
   * Performs the replacement step of the genetic algorithm for the given children instances.
   *
   * @param childrenInstances the instances of the child population
   * @throws AExOpDCSException if there is an error executing the replace operation
   */
  public void makeReplace(PopulationInstances childrenInstances) throws AExOpDCSException {
    if (childrenInstances == null) {
      return;
    }

    LOGGER.debug("Starting replace step for {} family", attributeDCS.getType());
    long startTime = System.currentTimeMillis();
    IMDGAReplace replaceSubPopulation =
        MDGAReplaceFactory.getRecombination(dcsEvolutiveConfig.getReplaceSubConf());
    bestSubset =
        replaceSubPopulation.makeReplace(
            attributeDCS.getType(), bestSubset, parents, childrenInstances);

    AbstractMDFilter filters = FilterFactory.getFilters(dcsEvolutiveConfig.getFiltersConfig());
    filters.filtering(bestSubset);
    LOGGER.debug(
        "Completed replace md operation for {} family in {} ms ",
        attributeDCS.getType(),
        System.currentTimeMillis() - startTime);
  }

  public void geneticsOperators(int nParents) throws AExOpDCSException {
    parents = new LinkedList<>();
    children = new LinkedHashSet<>();

    int numParents = nParents == 0 ? selectionOperator.getCant() : nParents;
    numParents = numParents % 2 == 0 ? numParents : numParents + 1;
    // selecting parents

    selectionOperator.build(bestSubset);

    // make crossover
    LOGGER.debug("Starting selection and crossover step for {} family", attributeDCS.getType());
    long selCro = System.currentTimeMillis();
    executeCrossOver(numParents);
    long intermediateTime = System.currentTimeMillis();
    LOGGER.debug(
        "Completed selection and crossover operations for {} family in {} ms ",
        attributeDCS.getType(),
        intermediateTime - selCro);
    LOGGER.debug("Starting mutation step for {} family", attributeDCS.getType());

    for (AHeadEntity head : children) {
      AGAMutation mutation =
          GAMutationFactory.getMutation(dcsEvolutiveConfig.getMutConf(), dcsFactory);
      mutation.mutation(head);
    }

    children =
        new LinkedHashSet<>(
            new LinkedList<>(children).subList(0, Math.min(nParents, children.size())));
    if (children.isEmpty())
      System.out.println("Error in mutation operators, no children generated");

    LOGGER.debug(
        "Completed mutation operation for {} family in {} ms ",
        attributeDCS.getType(),
        System.currentTimeMillis() - intermediateTime);

    writeFileHead(children);
  }

  private AHeadEntity getHead4Pos(int pos) throws AExOpDCSException {
    String name = bestSubset.attribute(pos).name();
    AHeadEntity head = headFactory.getHead(attributeDCS.getType());
    head.setFromString(name);
    return head;
  }

  private int getHammingThreshold(AHeadEntity headP1, AHeadEntity headP2) {
    Map<String, String> pA1 = headP1.parseHead2Map();
    Map<String, String> pA2 = headP2.parseHead2Map();

    // get de head with max number of params
    Set<String> keys = new LinkedHashSet<>(pA1.keySet());
    keys.addAll(pA2.keySet());
    return keys.size() / 4;
  }

  private void executeCrossOver(int numParents) {
    for (int i = 0; i < numParents / 2; i++) {
      try {
        int p1 = selectUniqueParent();
        AHeadEntity headP1 = getHead4Pos(p1);
        int p2 = selectUniqueParent(headP1);
        parents.add(p1);
        parents.add(p2);

        AHeadEntity headP2 = getHead4Pos(p2);

        LOGGER.debug(
            "Selected parents {} and {} for {} family", headP1, headP2, attributeDCS.getType());

        AGACrossoverOperation crossoverOperation =
            GACrossoverFactory.getCrossover(dcsEvolutiveConfig.getCrossConf(), headFactory);
        children.addAll(crossoverOperation.makeCrossover(headP1, headP2));
        if (children.isEmpty())
          System.out.println("Error in crossover operators, no children generated");
      } catch (Exception ex) {
        throw AExOpDCSException.ExceptionType.DCS_EVOLUTION_EXCEPTION.get(
            "Problems getting offspring list", ex);
      }
    }
  }

  public Set<String> getChildrenAsString() {
    return children.stream().map(AHeadEntity::toString).collect(Collectors.toSet());
  }

  private int selectUniqueParent() throws AExOpDCSException {
    int p;
    AHeadEntity head;
    do {
      p = selectionOperator.getOneParent();
      head = getHead4Pos(p);
      String headP1 = head.toString();
      LOGGER.debug(MSG_SEL, headP1, attributeDCS.getType());
    } while (parents.contains(p));
    return p;
  }

  private int selectUniqueParent(AHeadEntity headP1) throws AExOpDCSException {
    int p;
    int d;
    AHeadEntity head;
    do {
      p = selectionOperator.getOneParent();
      head = getHead4Pos(p);
      d = getHammingThreshold(headP1, head);
      LOGGER.debug(
          "parents {} and {} are close for {} family", headP1, head, attributeDCS.getType());
    } while (Statistics.hammingDistance(headP1, head) <= d || parents.contains(p));
    return p;
  }
}
