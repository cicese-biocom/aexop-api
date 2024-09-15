/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tomocomd.subsetsearch.filters;

import java.util.*;
import tomocomd.configuration.filters.ImputationMissingValuesType;
import tomocomd.configuration.filters.ImputationReplaceValue;
import tomocomd.data.PopulationInstances;
import tomocomd.exceptions.AExOpDCSException;
import tomocomd.utils.ParseOptions;
import tomocomd.utils.Statistics;

/**
 * @author Potter
 */
public class NaNFilter extends AbstractFilterDecorator {

  ImputationMissingValuesType type;
  ImputationReplaceValue value;

  public NaNFilter(AbstractMDFilter filter) {
    super(filter);
    level = 1;
  }

  @Override
  protected int[] getRemovePosition(PopulationInstances data) throws AExOpDCSException {
    List<Integer> pos = new LinkedList<>();
    if (Objects.isNull(data)) return new int[] {};
    if (data.numInstances() < 0) {
      return new int[] {};
    }

    try {
      boolean flg;
      int numAtt = data.numAttributes();
      int tIdx = data.classIndex();
      for (int i = 0; i < numAtt; i++) {
        if (i != tIdx) {
          double[] vAtt = data.attributeToDoubleArray(i);
          flg = Arrays.stream(vAtt).anyMatch(Double::isNaN);

          if (flg) {
            if (type == ImputationMissingValuesType.DELETE) {
              pos.add(i);
            } else {
              setImputationValue(i, vAtt, data);
            }
          }
        }
      }
    } catch (Exception ex) {
      throw AExOpDCSException.ExceptionType.FILTER_EXCEPTION.get(
          "Problems computing NaN filter", ex);
    }

    return pos.stream().mapToInt(Integer::intValue).toArray();
  }

  private void setImputationValue(int i, double[] values, PopulationInstances data) {
    double valueToSus = getImputationValue(values);
    for (int j = 0; j < data.numInstances(); j++) {
      if (((Double) values[j]).isNaN()) {
        data.instance(j).setValue(i, valueToSus);
      }
    }
  }

  private double getImputationValue(double[] values) throws AExOpDCSException {
    double[] valuesWithOutNan = Arrays.stream(values).filter(v -> !Double.isNaN(v)).toArray();
    switch (value) {
      case ZERO:
        return 0;
      case MEAN:
        double valueToReplaceMean = Statistics.average(valuesWithOutNan);
        if (Double.isNaN(valueToReplaceMean)) valueToReplaceMean = 0;
        return valueToReplaceMean;
      case MEDIAN:
        double valueToReplaceMedian = Statistics.median(valuesWithOutNan);
        if (Double.isNaN(valueToReplaceMedian)) valueToReplaceMedian = 0;
        return valueToReplaceMedian;
      default:
        throw AExOpDCSException.ExceptionType.FILTER_EXCEPTION.get(
            "Imputation missing value need imputation value");
    }
  }

  @Override
  public void setOptions(String[] options) throws AExOpDCSException {
    if (options.length != 2 && options.length != 4) {
      throw AExOpDCSException.ExceptionType.FILTER_EXCEPTION.get(
          "Delete imputation function need imputation type and imputation value");
    }

    Map<String, String> opts;
    try {
      opts = ParseOptions.getOption(options);
    } catch (Exception e) {
      throw AExOpDCSException.ExceptionType.FILTER_EXCEPTION.get(
          "Error parsing NaN filter options", e);
    }
    type = null;
    try {
      type = ImputationMissingValuesType.valueOf(opts.get("-t"));
    } catch (Exception ex) {
      throw AExOpDCSException.ExceptionType.FILTER_EXCEPTION.get(
          "Imputation missing value need imputation type", ex);
    }

    value = null;
    if (type == ImputationMissingValuesType.IMPUTATION) {
      try {
        value = ImputationReplaceValue.valueOf(opts.get("-v"));
      } catch (Exception ex) {
        throw AExOpDCSException.ExceptionType.FILTER_EXCEPTION.get(
            String.format("Imputation missing %s neeed a imputation value", type), ex);
      }
    }
  }
}
