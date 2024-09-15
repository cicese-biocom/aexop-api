/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tomocomd.subsetsearch.evaluation.attributeevaluation.fuzzylogic.fuzzymeasures;

import tomocomd.configuration.evaluation.fussylogic.FuzzyMeasureConf;
import tomocomd.exceptions.AExOpDCSException;

/**
 * @author potter
 */
public class FuzzyMeasureFactory {
  private FuzzyMeasureFactory() {
    throw new IllegalStateException();
  }

  public static AFuzzyMeasure getFuzzyMeasure(FuzzyMeasureConf conf) throws AExOpDCSException {
    switch (conf.getType()) {
      case LAMBDA:
        return new SugenoMeasure(conf);
      case Q:
        return new QMeasure(conf);
      default:
        throw AExOpDCSException.ExceptionType.FUZZY_MEASURE_EXCEPTION.get(
            String.format("Function %s is not defined", conf.getType()));
    }
  }
}
