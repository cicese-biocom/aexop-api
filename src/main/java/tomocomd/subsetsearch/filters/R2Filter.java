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
public class R2Filter extends AbstractFilterDecorator {

  private double threshold;

  public R2Filter(AbstractMDFilter filter) {
    super(filter);
    threshold = 0;
    level = 1;
  }

  @Override
  public void setOptions(String[] options) throws AExOpDCSException {
    if (options.length != 2) {
      throw AExOpDCSException.ExceptionType.FILTER_EXCEPTION.get("R2 filter need threshold value");
    }
    if (!(options[0].equals("-t"))) {
      throw AExOpDCSException.ExceptionType.FILTER_EXCEPTION.get("R2 filter need threshold value");
    }
    try {
      threshold = Double.parseDouble(options[1]);
    } catch (NumberFormatException ex) {
      throw AExOpDCSException.ExceptionType.FILTER_EXCEPTION.get(
          "R2 filter need a double for threshold value", ex);
    }
  }

  @Override
  protected int[] getRemovePosition(PopulationInstances data) throws AExOpDCSException {
    int numAtt = data.numAttributes();
    if (numAtt < 2 || data.classIndex() < 0 || data.numInstances() < 1) {
      return new int[0];
    }

    List<Integer> pos = new LinkedList<>();

    int targetIndex = data.classIndex();
    double[] targetValues = data.attributeToDoubleArray(targetIndex);

    try {
      for (int i = 0; i < numAtt; i++) {
        if (i != data.classIndex()
            && Statistics.r2(data.attributeToDoubleArray(i), targetValues) < threshold) {
          pos.add(i);
        }
      }
    } catch (Exception ex) {
      throw AExOpDCSException.ExceptionType.FILTER_EXCEPTION.get("Error computing R2 filter", ex);
    }

    return pos.stream().mapToInt(Integer::intValue).toArray();
  }
}
