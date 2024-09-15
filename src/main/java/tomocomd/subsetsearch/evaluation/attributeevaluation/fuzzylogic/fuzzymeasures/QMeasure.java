/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tomocomd.subsetsearch.evaluation.attributeevaluation.fuzzylogic.fuzzymeasures;

import lombok.Data;
import tomocomd.configuration.evaluation.fussylogic.FuzzyMeasureConf;
import tomocomd.exceptions.AExOpDCSException;

/**
 * @author potter
 */
@Data
public class QMeasure extends SugenoMeasure {

  private double norm;

  public QMeasure() {
    super();
  }

  public QMeasure(FuzzyMeasureConf conf) throws AExOpDCSException {
    setOptions(conf.getOptions());
  }

  @Override
  public void buildMeasure() throws AExOpDCSException {
    try {
      double den = 1;
      for (double v : densitiesValues) {
        den *= (1 + lambdaValues * v);
      }
      norm = den - 1;
    } catch (Exception ex) {
      throw AExOpDCSException.ExceptionType.FUZZY_MEASURE_EXCEPTION.get(
          "Problems building QMeasure function", ex);
    }
  }

  @Override
  public double evaluateSubset(Integer[] pos) throws AExOpDCSException {
    try {
      double num = 1;
      for (int i : pos) {
        num *= (1 + lambdaValues * densitiesValues[i]);
      }
      return (num - 1) / norm;
    } catch (Exception ex) {
      throw AExOpDCSException.ExceptionType.FUZZY_MEASURE_EXCEPTION.get(
          "Problems evaluating subset of density values in QMeasure function", ex);
    }
  }
}
