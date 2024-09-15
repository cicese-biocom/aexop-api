package tomocomd.subsetsearch.evaluation.attributeevaluation;

import lombok.Getter;
import tomocomd.configuration.evaluation.mdevaluation.AttributeEvaluationType;
import tomocomd.data.PopulationInstances;
import tomocomd.exceptions.AExOpDCSException;

public abstract class AAtributeEvaluation {

  private static final String MSG_ERR = "Problems got %s quality of attribute %d";
  @Getter protected double[] eva;
  protected int idx;

  public abstract void buildEvaluator(PopulationInstances i) throws AExOpDCSException;

  public double evaluate(int i) throws AExOpDCSException {
    double res;
    try {
      res = eva[i];
      return res;
    } catch (Exception ex) {
      throw AExOpDCSException.ExceptionType.MD_EVALUATION_FUNCTION_EXCEPTION.get(
          String.format(MSG_ERR, getClass().getSimpleName(), i), ex);
    }
  }

  public abstract void setOptions(String[] strings) throws AExOpDCSException;

  public abstract String[] getOptions();

  public abstract AttributeEvaluationType getType();
}
