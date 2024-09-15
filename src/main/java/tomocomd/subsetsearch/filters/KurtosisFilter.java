/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tomocomd.subsetsearch.filters;

import java.util.LinkedList;
import java.util.List;
import tomocomd.data.PopulationInstances;
import tomocomd.exceptions.AExOpDCSException;
import tomocomd.utils.Statistics;

/**
 * @author Potter
 */
public class KurtosisFilter extends AbstractFilterDecorator {

  double threshold;

  public KurtosisFilter(AbstractMDFilter filter) throws AExOpDCSException {
    super(filter);
    threshold = 0;
    level = 1;
  }

  @Override
  public void setOptions(String[] options) throws AExOpDCSException {
    if (options.length != 2) {
      throw AExOpDCSException.ExceptionType.FILTER_EXCEPTION.get(
          "Kurtosis filter need threshold value");
    }
    if (!(options[0].equals("-t"))) {
      throw AExOpDCSException.ExceptionType.FILTER_EXCEPTION.get(
          "Kurtosis filter need threshold value");
    }
    try {
      threshold = Double.parseDouble(options[1]);
    } catch (NumberFormatException ex) {
      throw AExOpDCSException.ExceptionType.FILTER_EXCEPTION.get(
          "Kurtosis filter need a double for threshold value");
    }
  }

  @Override
  protected int[] getRemovePosition(PopulationInstances data) throws AExOpDCSException {

    if (data.numInstances() < 0) {
      return new int[0];
    }

    int numAtt = data.numAttributes();
    double v;
    List<Integer> pos = new LinkedList<>();

    try {
      for (int i = 0; i < numAtt; i++) {
        if (i != data.classIndex()) {
          v = Statistics.kurtosis(data.attributeToDoubleArray(i));
          if (v > threshold) {
            pos.add(i);
          }
        }
      }
    } catch (Exception ex) {
      throw AExOpDCSException.ExceptionType.FILTER_EXCEPTION.get(
          "Problems computing Kurtosis filter", ex);
    }
    return pos.stream().mapToInt(Integer::intValue).toArray();
  }
}
