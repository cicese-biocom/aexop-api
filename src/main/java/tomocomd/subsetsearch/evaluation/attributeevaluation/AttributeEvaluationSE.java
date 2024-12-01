package tomocomd.subsetsearch.evaluation.attributeevaluation;

import tomocomd.configuration.evaluation.attributeevaluation.AttributeEvaluationType;
import tomocomd.data.PopulationInstances;
import tomocomd.exceptions.AExOpDCSException;
import tomocomd.utils.Statistics;

public class AttributeEvaluationSE extends AAtributeEvaluation {

  public AttributeEvaluationSE() {
    super();
  }

  @Override
  public void buildEvaluator(PopulationInstances insets) throws AExOpDCSException {
    try {
      int numDesc = insets.numAttributes();
      int numInsets = insets.numInstances();
      eva = new double[numDesc];
      double maxSE = Statistics.log2(numInsets);

      idx = insets.classIndex();
      for (int i = 0; i < numDesc; i++) {
        if (i == idx) eva[i] = 0;
        else eva[i] = Statistics.se(insets.attributeToDoubleArray(i)) / maxSE;
        insets.setEva4DescPos(i, eva[i]);
      }
    } catch (Exception ex) {
      throw AExOpDCSException.ExceptionType.MD_EVALUATION_FUNCTION_EXCEPTION.get(
          "Problems building Shannon entropy function", ex);
    }
  }

  @Override
  public void setOptions(String[] strings) throws AExOpDCSException {
    throw new UnsupportedOperationException();
  }

  @Override
  public String[] getOptions() {
    return new String[0];
  }

  @Override
  public AttributeEvaluationType getType() {
    return AttributeEvaluationType.SE;
  }
}
