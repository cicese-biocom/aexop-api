package tomocomd.subsetsearch.evaluation.subsetevaluation;

import java.util.*;
import lombok.extern.slf4j.Slf4j;
import tomocomd.configuration.evaluation.subsetevaluation.SubsetEvaluationConfig;
import tomocomd.data.PopulationInstances;
import tomocomd.exceptions.AExOpDCSException;
import tomocomd.utils.ParseOptions;
import tomocomd.utils.SortInstancesByAttribute;
import tomocomd.utils.Statistics;
import weka.attributeSelection.ASEvaluation;
import weka.attributeSelection.SubsetEvaluator;
import weka.core.Utils;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Reorder;

@Slf4j
public class MeanPlusSTDRankingEvaluateSubset implements IEvaluateSubset {

  private final SubsetEvaluationConfig conf;
  private final double thresholdCor;

  public MeanPlusSTDRankingEvaluateSubset(SubsetEvaluationConfig conf) throws AExOpDCSException {
    this.conf = conf;
    thresholdCor = Double.parseDouble(ParseOptions.getOption("-c", conf.getOptions()));
  }

  @Override
  public PopulationInstances bestSubset(PopulationInstances data) throws AExOpDCSException {

    PopulationInstances inst = new PopulationInstances(data);
    // get md importance and sorted it
    double[] fitness = inst.getEva4Desc();
    List<Integer> pos = SortInstancesByAttribute.sortAndGetPos(fitness, true);
    pos.remove(pos.indexOf(inst.classIndex()));
    pos.add(0, inst.classIndex());

    // order att in data by pos
    try {
      Reorder reorder = new Reorder();
      reorder.setAttributeIndicesArray(pos.stream().mapToInt(Integer::intValue).toArray());
      reorder.setInputFormat(inst);
      inst = new PopulationInstances(Filter.useFilter(inst, reorder));
    } catch (Exception ex) {
      throw AExOpDCSException.ExceptionType.SUBSET_EVALUATE_EXCEPTION.get(
          "Error getting best subset", ex);
    }

    // sort fitness values
    double[] fitnessWithOutClass = new double[fitness.length - 1];
    System.arraycopy(fitness, 1, fitnessWithOutClass, 0, fitnessWithOutClass.length);
    fitnessWithOutClass =
        Arrays.stream(fitnessWithOutClass)
            .boxed()
            .sorted(Collections.reverseOrder())
            .mapToDouble(Double::doubleValue)
            .toArray();
    System.arraycopy(fitnessWithOutClass, 0, fitness, 1, fitnessWithOutClass.length);
    inst.setEva4Desc(fitness);

    // get correlated md
    List<Integer> pos2Del = new LinkedList<>();
    int numAtt = inst.numAttributes();

    for (int i = 0; i < numAtt; i++) {
      if (!pos2Del.contains(i)) {
        for (int j = numAtt - 1; j > i; j--) {
          if (!pos2Del.contains(i)
              && i != inst.classIndex()
              && j != inst.classIndex()
              && Utils.correlation(
                      inst.attributeToDoubleArray(i),
                      inst.attributeToDoubleArray(j),
                      inst.numInstances())
                  > thresholdCor) {
            pos2Del.add(j);
          }
        }
      }
    }

    // remove correlated md
    try {
      inst.deleteAttributesByPos(new LinkedHashSet<>(pos2Del), false);
    } catch (Exception e) {
      throw AExOpDCSException.ExceptionType.SUBSET_EVALUATE_EXCEPTION.get(
          "Error deleting correlated md", e);
    }

    double meanStd = 0;
    try {
      double[] evaA = new double[inst.getEva4Desc().length - 1];
      int evaCursor = 0;
      for (int i = 0; i < inst.getEva4Desc().length; i++)
        if (i != inst.classIndex()) evaA[evaCursor++] = inst.getEva4Desc(i);

      double mean = Statistics.average(evaA);
      double std = Statistics.std(evaA);
      meanStd = mean + std;
    } catch (Exception ex) {
      log.error("Error computing statistics values", ex);
    }

    // remove desc with quality lower than mean+std
    Set<Integer> pos2rem = new LinkedHashSet<>();
    for (int i = 0; i < inst.numAttributes(); i++) {
      if (i != inst.classIndex() && inst.getEva4Desc(i) < meanStd) pos2rem.add(i);
    }

    try {
      inst.deleteAttributesByPos(pos2rem, false);
    } catch (Exception e) {
      throw AExOpDCSException.ExceptionType.SUBSET_EVALUATE_EXCEPTION.get(
          "Error deleting extra md", e);
    }

    try {
      evaluateSubset(inst);
    } catch (Exception e) {
      throw AExOpDCSException.ExceptionType.SUBSET_EVALUATE_EXCEPTION.get(
          "Error evaluating fitness subset", e);
    }
    return inst;
  }

  private void evaluateSubset(PopulationInstances data) throws Exception {
    ASEvaluation eva =
        SubsetFitnessFactory.getSubsetEvaluator(conf.getSubSetFitnessConf().getType());
    eva.buildEvaluator(data);

    BitSet bitSet = new BitSet(data.numAttributes());
    bitSet.set(0, data.numAttributes(), true);

    data.setEvaSub(((SubsetEvaluator) eva).evaluateSubset(bitSet));
  }
}
