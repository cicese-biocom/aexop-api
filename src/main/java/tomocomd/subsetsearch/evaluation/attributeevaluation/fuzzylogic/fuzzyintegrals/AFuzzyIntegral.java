/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tomocomd.subsetsearch.evaluation.attributeevaluation.fuzzylogic.fuzzyintegrals;

import lombok.Data;
import tomocomd.configuration.evaluation.fussylogic.FuzzyIntegralConf;
import tomocomd.exceptions.AExOpDCSException;
import tomocomd.subsetsearch.evaluation.attributeevaluation.fuzzylogic.fuzzymeasures.AFuzzyMeasure;
import tomocomd.subsetsearch.evaluation.attributeevaluation.fuzzylogic.fuzzymeasures.FuzzyMeasureFactory;

/**
 * @author potter
 */
@Data
public abstract class AFuzzyIntegral {
  protected AFuzzyMeasure measure;

  protected AFuzzyIntegral(FuzzyIntegralConf conf) throws AExOpDCSException {
    this.measure = FuzzyMeasureFactory.getFuzzyMeasure(conf.getConfMeas());
  }

  protected AFuzzyIntegral() {}

  public void setDensityValues(double[] densityValues) throws AExOpDCSException {
    try {
      measure.setDensitiesValues(densityValues);
      measure.buildMeasure();
    } catch (Exception ex) {
      throw AExOpDCSException.ExceptionType.FUZZY_INTEGRAL_EXCEPTION.get(
          "Problems set densities values", ex);
    }
  }

  public abstract double compute(double[] values) throws AExOpDCSException;
}
