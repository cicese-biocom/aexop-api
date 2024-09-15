/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tomocomd.subsetsearch.evaluation.attributeevaluation.fuzzylogic.fuzzymeasures;

import java.util.Map;
import lombok.Data;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import tomocomd.configuration.evaluation.fussylogic.FuzzyMeasureConf;
import tomocomd.exceptions.AExOpDCSException;
import tomocomd.utils.ParseOptions;

/**
 * @author potter
 */
@Data
public class SugenoMeasure extends AFuzzyMeasure {

  protected double lambdaValues;

  public SugenoMeasure() {
    super();
  }

  public SugenoMeasure(FuzzyMeasureConf conf) throws AExOpDCSException {
    setOptions(conf.getOptions());
  }

  @Override
  public void buildMeasure() throws AExOpDCSException {
    try {
      PolynomialFunction resto = new PolynomialFunction(new double[] {1, 1});

      PolynomialFunction mulPoly = new PolynomialFunction(new double[] {1, densitiesValues[0]});
      for (int i = 1; i < densitiesValues.length; i++) {
        PolynomialFunction temp = new PolynomialFunction(new double[] {1, densitiesValues[i]});
        mulPoly = mulPoly.multiply(temp);
      }

      mulPoly = mulPoly.subtract(resto);
      updateLambda(mulPoly.getCoefficients());
    } catch (NullPointerException ex) {
      throw AExOpDCSException.ExceptionType.FUZZY_MEASURE_EXCEPTION.get(
          "Problems building  Sugeno lambda measure function", ex);
    }
  }

  private void updateLambda(double[] poly) {

    int n = poly.length - 1;
    double tmp;
    lambdaValues = -1;

    double[][] d = new double[n][n];
    double a = poly[n];
    for (int i = 0; i < n; i++) {
      d[i][n - 1] = -poly[i] / a;
    }
    for (int i = 1; i < n; i++) {
      d[i][i - 1] = 1;
    }

    RealMatrix matrix = new Array2DRowRealMatrix(d, false);
    EigenDecomposition des = new EigenDecomposition(matrix);

    for (int i = 0; i < n; i++) {
      tmp = Math.round((des.getRealEigenvalue(i) * 10000d)) / 10000d;
      if (tmp > lambdaValues && tmp != 0) {
        lambdaValues = des.getRealEigenvalue(i);
      }
    }
  }

  @Override
  public double evaluateSubset(Integer[] pos) throws AExOpDCSException {
    try {
      double mult = 1;

      if (pos.length == 1) return densitiesValues[pos[0]];

      for (int v : pos) {
        mult *= (densitiesValues[v] * lambdaValues + 1);
      }
      return (mult - 1) / lambdaValues;
    } catch (Exception ex) {
      throw AExOpDCSException.ExceptionType.FUZZY_MEASURE_EXCEPTION.get(
          "Problems evaluating subset of density values in Sugeno function", ex);
    }
  }

  @Override
  public void setOptions(String[] opts) throws AExOpDCSException {
    try {
      Map<String, String> optsValues = ParseOptions.getOption(opts);
      lambdaValues = Double.parseDouble(optsValues.get("-l"));
      String dValues = optsValues.get("-d");
      String[] dValuesS = dValues.substring(1, dValues.length() - 1).split(";");
      densitiesValues = new double[dValuesS.length];
      int cont = 0;
      for (String dvs : dValuesS) densitiesValues[cont++] = Double.parseDouble(dvs);
    } catch (Exception ex) {
      throw AExOpDCSException.ExceptionType.PARSE_EXCEPTION.get(
          "Error getting fuzzy measure options");
    }
  }
}
