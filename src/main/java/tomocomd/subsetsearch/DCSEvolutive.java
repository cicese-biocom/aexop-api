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
import tomocomd.configuration.dcs.HeadFactory;
import tomocomd.configuration.subsetsearch.DCSEvolutiveConfig;
import tomocomd.configuration.subsetsearch.operators.GASelectionType;
import tomocomd.data.PopulationInstances;
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
  private final AAttributeDCS amdHead;

  private List<Integer> parents;
  private Set<AHeadEntity> children;
  private final AbstractGASelectionOperator selectionOperator;
  //  subsets
  @Getter private PopulationInstances bestSubset;
  private final List<String> generatedHeads;

  public DCSEvolutive(DCSEvolutiveConfig gaAlgorithm4PobConf, AAttributeDCS amdHead) {

    this.dcsEvolutiveConfig = gaAlgorithm4PobConf;
    this.amdHead = amdHead;
    selectionOperator = GASelectionFactory.selectionCreator(gaAlgorithm4PobConf.getSelConf());

    File folder = new File(FOLDER_NAME_MD);
    if (!folder.exists()) {
      boolean created = folder.mkdir();
      if (!created) LOGGER.warn("Folder {} not created", FOLDER_NAME_MD);
    }
    nameGeneratedFile =
        new File(
                new File(FOLDER_NAME_MD),
                String.format("%s_%d.txt", amdHead.getType(), System.currentTimeMillis()))
            .getAbsolutePath();
    generatedHeads = new LinkedList<>();
    LOGGER.info("DCS for {} objected created", amdHead.getType());
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
      nSHead = amdHead.randomHeading();
      while (generatedHeads.contains(nSHead.toString())) nSHead = amdHead.randomHeading();
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
            amdHead.getType());
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
            amdHead.getType());
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

    LOGGER.debug("Starting replace step for {} family", amdHead.getType());
    long startTime = System.currentTimeMillis();
    IMDGAReplace replaceSubPopulation =
        MDGAReplaceFactory.getRecombination(dcsEvolutiveConfig.getReplaceSubConf());
    bestSubset =
        replaceSubPopulation.makeReplace(amdHead.getType(), bestSubset, parents, childrenInstances);

    AbstractMDFilter filters = FilterFactory.getFilters(dcsEvolutiveConfig.getFiltersConfig());
    filters.filtering(bestSubset);
    LOGGER.debug(
        "Completed replace md operation for {} family in {} ms ",
        amdHead.getType(),
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
    LOGGER.debug("Starting selection and crossover step for {} family", amdHead.getType());
    long selCro = System.currentTimeMillis();
    executeCrossOver(numParents);
    long intermediateTime = System.currentTimeMillis();
    LOGGER.debug(
        "Completed selection and crossover operations for {} family in {} ms ",
        amdHead.getType(),
        intermediateTime - selCro);
    LOGGER.debug("Starting mutation step for {} family", amdHead.getType());

    for (AHeadEntity head : children) {
      AGAMutation mutation = GAMutationFactory.getMutation(dcsEvolutiveConfig.getMutConf());
      mutation.mutation(head);
    }

    children =
        new LinkedHashSet<>(
            new LinkedList<>(children).subList(0, Math.min(nParents, children.size())));

    LOGGER.debug(
        "Completed mutation operation for {} family in {} ms ",
        amdHead.getType(),
        System.currentTimeMillis() - intermediateTime);

    writeFileHead(children);
  }

  private AHeadEntity getHead4Pos(int pos) throws AExOpDCSException {
    String name = bestSubset.attribute(pos).name();
    AHeadEntity head = HeadFactory.getHead(amdHead.getType());
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

        LOGGER.debug("Selected parents {} and {} for {} family", headP1, headP2, amdHead.getType());

        AGACrossoverOperation crossoverOperation =
            GACrossoverFactory.getCrossover(dcsEvolutiveConfig.getCrossConf());
        children.addAll(crossoverOperation.makeCrossover(headP1, headP2));
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
      LOGGER.debug(MSG_SEL, headP1, amdHead.getType());
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
      LOGGER.debug("parents {} and {} are close for {} family", headP1, head, amdHead.getType());
    } while (Statistics.hammingDistance(headP1, head) < d || parents.contains(p));
    return p;
  }
}
