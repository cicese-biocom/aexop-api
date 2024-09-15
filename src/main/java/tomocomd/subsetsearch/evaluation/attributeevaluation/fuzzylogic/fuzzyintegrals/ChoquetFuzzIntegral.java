/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tomocomd.subsetsearch.evaluation.attributeevaluation.fuzzylogic.fuzzyintegrals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import tomocomd.configuration.evaluation.fussylogic.FuzzyIntegralConf;
import tomocomd.configuration.evaluation.fussylogic.FuzzyMeasureConf;
import tomocomd.exceptions.AExOpDCSException;
import tomocomd.subsetsearch.evaluation.attributeevaluation.fuzzylogic.fuzzymeasures.FuzzyMeasureFactory;

/**
 * @author potter
 */
public class ChoquetFuzzIntegral extends AFuzzyIntegral {

  public ChoquetFuzzIntegral(FuzzyIntegralConf conf) throws AExOpDCSException {
    super(conf);
  }

  public ChoquetFuzzIntegral(FuzzyMeasureConf conf) throws AExOpDCSException {
    super();
    this.measure = FuzzyMeasureFactory.getFuzzyMeasure(conf);
  }

  @Override
  public double compute(double[] values) throws AExOpDCSException {
    try {
      measure.buildMeasure();

      Map<Integer, Double> valuesMap = new HashMap<>();
      int pos = 0;
      for (double v : values) {
        valuesMap.put(pos++, v);
      }

      Map<Integer, Double> sortedValues =
          valuesMap.entrySet().stream()
              .sorted(Map.Entry.comparingByValue())
              .collect(
                  Collectors.toMap(
                      Map.Entry::getKey,
                      Map.Entry::getValue,
                      (oldValue, newValue) -> oldValue,
                      LinkedHashMap::new));

      Integer[] posValues = sortedValues.keySet().toArray(new Integer[0]);
      double withOutDesc = 0;
      double sum = 0;

      for (int i = 0; i < values.length; i++) {
        Integer[] posValuesRed = Arrays.copyOfRange(posValues, 0, i + 1);
        double withDesc = measure.evaluateSubset(posValuesRed);
        sum += (withDesc - withOutDesc) * values[posValues[i]];
        withOutDesc = withDesc;
      }
      return sum;
    } catch (Exception ex) {
      throw AExOpDCSException.ExceptionType.FUZZY_INTEGRAL_EXCEPTION.get(
          "Problems computing fuzzy integral", ex);
    }
  }
}
