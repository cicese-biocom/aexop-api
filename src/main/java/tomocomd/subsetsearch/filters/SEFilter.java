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
public class SEFilter extends AbstractFilterDecorator {

  private double threshold;

  public SEFilter(AbstractMDFilter filter) {
    super(filter);
    threshold = 0;
    level = 1;
  }

  @Override
  protected int[] getRemovePosition(PopulationInstances data) throws AExOpDCSException {
    try {
      if (data == null) return new int[] {};

      if (data.numInstances() < 1) {
        return new int[] {};
      }

      double maxSE = Statistics.log2(data.numInstances());
      int numAtt = data.numAttributes();
      List<Integer> pos = new LinkedList<>();

      for (int i = 0; i < numAtt; i++) {
        if (i != data.classIndex()
            && Statistics.se(data.attributeToDoubleArray(i)) < maxSE * threshold) {
          pos.add(i);
        }
      }
      return pos.stream().mapToInt(Integer::intValue).toArray();
    } catch (Exception ex) {
      throw AExOpDCSException.ExceptionType.FILTER_EXCEPTION.get(
          "Problems computing SE filter", ex);
    }
  }

  @Override
  public void setOptions(String[] options) throws AExOpDCSException {
    if (options.length != 2) {
      throw AExOpDCSException.ExceptionType.FILTER_EXCEPTION.get(
          "Shannon Entropy filter need threshold value");
    }
    if (!(options[0].equals("-t"))) {
      throw AExOpDCSException.ExceptionType.FILTER_EXCEPTION.get(
          "Shannon Entropy filter need threshold value");
    }
    try {
      threshold = Double.parseDouble(options[1]);
    } catch (NumberFormatException ex) {
      throw AExOpDCSException.ExceptionType.FILTER_EXCEPTION.get(
          "Shannon Entropy filter need a double for threshold value", ex);
    }
  }
}
