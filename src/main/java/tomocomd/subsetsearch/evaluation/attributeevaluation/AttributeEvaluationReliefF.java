package tomocomd.subsetsearch.evaluation.attributeevaluation;

import tomocomd.configuration.evaluation.attributeevaluation.AttributeEvaluationType;
import tomocomd.data.PopulationInstances;
import tomocomd.exceptions.AExOpDCSException;
import weka.attributeSelection.ReliefFAttributeEval;

public class AttributeEvaluationReliefF extends AAtributeEvaluation {

  private ReliefFAttributeEval r;

  public AttributeEvaluationReliefF() {
    super();
  }

  @Override
  public void buildEvaluator(PopulationInstances i) throws AExOpDCSException {
    r = new ReliefFAttributeEval();
    idx = i.classIndex();
    try {
      r.buildEvaluator(i);
    } catch (Exception ex) {
      throw AExOpDCSException.ExceptionType.MD_EVALUATION_FUNCTION_EXCEPTION.get(
          "Problems building ReliefF function", ex);
    }

    eva = new double[i.numAttributes()];
    for (int j = 0; j < i.numAttributes(); j++) {
      try {
        if (idx == j) eva[j] = 0;
        else eva[j] = r.evaluateAttribute(j);
        i.setEva4DescPos(j, eva[j]);
      } catch (Exception ex) {
        throw AExOpDCSException.ExceptionType.MD_EVALUATION_FUNCTION_EXCEPTION.get(
            String.format("Problems evaluating attribute %d by reliefF attribute evaluation", j),
            ex);
      }
    }
  }

  @Override
  public void setOptions(String[] strings) throws AExOpDCSException {
    try {
      r.setOptions(strings);
    } catch (Exception e) {
      throw AExOpDCSException.ExceptionType.MD_EVALUATION_FUNCTION_EXCEPTION.get(
          "Problems setting attribute options for ReliefF", e);
    }
  }

  @Override
  public String[] getOptions() {
    throw new UnsupportedOperationException(
        "Not supported yet."); // To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public AttributeEvaluationType getType() {
    return AttributeEvaluationType.RELIEFF;
  }
}
