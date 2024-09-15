package tomocomd.subsetsearch.evaluation.attributeevaluation;

import tomocomd.configuration.evaluation.mdevaluation.AttributeEvaluationType;
import tomocomd.data.PopulationInstances;
import tomocomd.exceptions.AExOpDCSException;
import weka.attributeSelection.CorrelationAttributeEval;

public class AttributeEvaluationR2 extends AAtributeEvaluation {

  public AttributeEvaluationR2() {
    super();
  }

  @Override
  public void buildEvaluator(PopulationInstances insts) throws AExOpDCSException {
    CorrelationAttributeEval cor = new CorrelationAttributeEval();

    try {
      cor.buildEvaluator(insts);
    } catch (Exception ex) {
      throw AExOpDCSException.ExceptionType.MD_EVALUATION_FUNCTION_EXCEPTION.get(
          "Problems building R2 function", ex);
    }

    eva = new double[insts.numAttributes()];
    idx = insts.classIndex();
    for (int j = 0; j < insts.numAttributes(); j++) {
      try {
        if (j == idx) eva[j] = 0;
        else eva[j] = cor.evaluateAttribute(j);
        insts.setEva4DescPos(j, eva[j]);

      } catch (Exception ex) {
        throw AExOpDCSException.ExceptionType.MD_EVALUATION_FUNCTION_EXCEPTION.get(
            String.format(
                "Problems evaluating attribute %d by Correlation attribute evaluation", j),
            ex);
      }
    }
  }

  @Override
  public void setOptions(String[] strings) throws AExOpDCSException {
    throw new UnsupportedOperationException(
        "Not supported yet."); // To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public String[] getOptions() {
    return new String[0];
  }

  @Override
  public AttributeEvaluationType getType() {
    return AttributeEvaluationType.R2;
  }
}
