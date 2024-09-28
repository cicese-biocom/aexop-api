/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tomocomd.subsetsearch.evaluation.attributeevaluation;

import tomocomd.configuration.evaluation.mdevaluation.AttributeEvaluationConf;
import tomocomd.exceptions.AExOpDCSException;

/**
 * @author Potter
 */
public class AttributeEvaluationFactory {

  private AttributeEvaluationFactory() {
    throw new IllegalStateException();
  }

  public static AAtributeEvaluation getAttributeQuality(AttributeEvaluationConf conf)
      throws AExOpDCSException {
    switch (conf.getType()) {
      case SE:
        return new AttributeEvaluationSE();
      case RELIEFF:
        AttributeEvaluationReliefF r = new AttributeEvaluationReliefF();
        r.setOptions(conf.getOption());
        return r;
      case R2:
        return new AttributeEvaluationR2();
      case IMPURITY:
        return new AttributeEvaluationImpurityDecrease();
      case CHOQUET:
        AttributeEvaluationChoquet cho = new AttributeEvaluationChoquet();
        cho.setOptions(conf.getOption());
        return cho;
      default:
        throw AExOpDCSException.ExceptionType.MD_EVALUATION_FUNCTION_EXCEPTION.get(
            String.format("Function %s is not defined", conf.getType()));
    }
  }
}
