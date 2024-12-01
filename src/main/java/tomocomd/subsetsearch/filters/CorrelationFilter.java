/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tomocomd.subsetsearch.filters;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;
import tomocomd.data.PopulationInstances;
import tomocomd.exceptions.AExOpDCSException;
import tomocomd.subsetsearch.evaluation.attributeevaluation.AttributeEvaluationSE;
import weka.core.Utils;

/**
 * @author Potter
 */
public class CorrelationFilter extends AbstractFilterDecorator {

  double threshold;

  public CorrelationFilter(AbstractMDFilter filter) {
    super(filter);
    threshold = 0;
    level = 1;
  }

  @Override
  public void setOptions(String[] options) throws AExOpDCSException {
    if (options.length != 2) {
      throw AExOpDCSException.ExceptionType.FILTER_EXCEPTION.get(
          "Correlation filter need threshold value");
    }
    if (!(options[0].equals("-t"))) {
      throw AExOpDCSException.ExceptionType.FILTER_EXCEPTION.get(
          "Correlation filter need threshold value");
    }
    try {
      threshold = Double.parseDouble(options[1]);
    } catch (NumberFormatException ex) {
      throw AExOpDCSException.ExceptionType.FILTER_EXCEPTION.get(
          "Correlation filter need a double for threshold value", ex);
    }
  }

  @Override
  protected int[] getRemovePosition(PopulationInstances data) throws AExOpDCSException {

    List<Integer> pos = new LinkedList<>();
    int numAtt = data.numAttributes();
    int i;
    int j;
    double seI;
    double seJ;
    if (numAtt < 2 || data.numInstances() < 1) {
      return new int[0];
    }

    AttributeEvaluationSE se = getSE(data);

    List<Integer> posJ = new LinkedList<>();
    IntStream.range(0, numAtt).forEach(posJ::add);

    int x;
    for (i = numAtt - 1; i > 0; i--) {
      x = posJ.get(i);
      for (j = i - 1; j >= 0; j--) {
        if (j != data.classIndex() && i != data.classIndex()) {
          try {
            double cor =
                Utils.correlation(
                    data.attributeToDoubleArray(x),
                    data.attributeToDoubleArray(j),
                    data.numInstances());
            if (Math.abs(cor) > threshold) {
              seI = se.evaluate(x);
              seJ = se.evaluate(j);
              if (seI >= seJ) {
                pos.add(j);
                posJ.remove(j);
                i--;
              } else {
                pos.add(x);
                posJ.remove((Integer) x);
                j = 0;
              }
            }
          } catch (AExOpDCSException ex) {
            throw AExOpDCSException.ExceptionType.FILTER_EXCEPTION.get(
                "Problems computing SE value for md", ex);
          } catch (Exception ex) {
            throw AExOpDCSException.ExceptionType.FILTER_EXCEPTION.get(
                "Problems filter objects", ex);
          }
        }
      }
    }
    return pos.stream().mapToInt(Integer::intValue).toArray();
  }

  private AttributeEvaluationSE getSE(PopulationInstances data) throws AExOpDCSException {
    AttributeEvaluationSE se = new AttributeEvaluationSE();
    try {
      se.buildEvaluator(data);
    } catch (Exception ex) {
      throw AExOpDCSException.ExceptionType.FILTER_EXCEPTION.get("Error building SE functione", ex);
    }
    return se;
  }
}
