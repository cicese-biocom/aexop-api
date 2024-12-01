package tomocomd.subsetsearch.evaluation.attributeevaluation;

import tomocomd.configuration.evaluation.attributeevaluation.AttributeEvaluationType;
import tomocomd.data.PopulationInstances;
import tomocomd.exceptions.AExOpDCSException;
import weka.classifiers.trees.RandomForest;

public class AttributeEvaluationImpurityDecrease extends AAtributeEvaluation {

  public AttributeEvaluationImpurityDecrease() {
    super();
  }

  @Override
  public void buildEvaluator(PopulationInstances i) throws AExOpDCSException {
    RandomForest rf = new RandomForest();
    rf.setComputeAttributeImportance(true);
    try {
      rf.buildClassifier(i);
    } catch (Exception e) {
      throw AExOpDCSException.ExceptionType.MD_EVALUATION_FUNCTION_EXCEPTION.get(
          "Problems building Impurity function", e);
    }

    try {
      eva = rf.computeAverageImpurityDecreasePerAttribute(null);
      i.setEva4Desc(eva);
    } catch (Exception e) {
      throw AExOpDCSException.ExceptionType.MD_EVALUATION_FUNCTION_EXCEPTION.get(
          "Problems evaluating attribute by Impurity evaluation", e);
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
    return AttributeEvaluationType.IMPURITY;
  }
}
