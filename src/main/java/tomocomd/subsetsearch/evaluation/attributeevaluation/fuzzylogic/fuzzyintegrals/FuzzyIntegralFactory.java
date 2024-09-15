package tomocomd.subsetsearch.evaluation.attributeevaluation.fuzzylogic.fuzzyintegrals;

import tomocomd.configuration.evaluation.fussylogic.FuzzyIntegralConf;
import tomocomd.configuration.evaluation.fussylogic.FuzzyIntegralType;
import tomocomd.exceptions.AExOpDCSException;

public class FuzzyIntegralFactory {

  private FuzzyIntegralFactory() {
    throw new IllegalStateException();
  }

  public static AFuzzyIntegral getFuzzyIntegral(FuzzyIntegralConf conf) throws AExOpDCSException {
    if (conf.getType() == FuzzyIntegralType.CHOQUET) return new ChoquetFuzzIntegral(conf);

    throw AExOpDCSException.ExceptionType.FUZZY_INTEGRAL_EXCEPTION.get(
        String.format("Fuzzy integral %s is not defined", conf.getType()));
  }
}
