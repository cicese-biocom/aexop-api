package tomocomd.subsetsearch.evaluation.attributeevaluation;

import java.util.Arrays;
import java.util.Map;
import tomocomd.configuration.evaluation.attributeevaluation.AttributeEvaluationType;
import tomocomd.configuration.evaluation.fussylogic.FuzzyIntegralConf;
import tomocomd.configuration.evaluation.fussylogic.FuzzyIntegralType;
import tomocomd.configuration.evaluation.fussylogic.FuzzyMeasureConf;
import tomocomd.configuration.evaluation.fussylogic.FuzzyMeasureType;
import tomocomd.data.PopulationInstances;
import tomocomd.exceptions.AExOpDCSException;
import tomocomd.subsetsearch.evaluation.attributeevaluation.fuzzylogic.fuzzyintegrals.AFuzzyIntegral;
import tomocomd.subsetsearch.evaluation.attributeevaluation.fuzzylogic.fuzzyintegrals.FuzzyIntegralFactory;
import tomocomd.utils.ParseOptions;

public class AttributeEvaluationChoquet extends AAtributeEvaluation {

  private FuzzyIntegralConf confFuzzy;

  public AttributeEvaluationChoquet() {
    super();
  }

  public AttributeEvaluationChoquet(FuzzyIntegralConf confFuzzy) {
    super();
    this.confFuzzy = confFuzzy;
  }

  @Override
  public void buildEvaluator(PopulationInstances data) throws AExOpDCSException {
    AttributeEvaluationReliefF relief = new AttributeEvaluationReliefF();
    AttributeEvaluationImpurityDecrease rf = new AttributeEvaluationImpurityDecrease();
    AttributeEvaluationSE se = new AttributeEvaluationSE();
    AttributeEvaluationR2 corr = new AttributeEvaluationR2();
    idx = data.classIndex();

    int numAtt = data.numAttributes();

    try {
      relief.buildEvaluator(data);
      se.buildEvaluator(data);
      corr.buildEvaluator(data);
      rf.buildEvaluator(data);
    } catch (Exception ex) {
      throw AExOpDCSException.ExceptionType.MD_EVALUATION_FUNCTION_EXCEPTION.get(
          "Problems building ChoquetIntegral function", ex);
    }

    double[] valuesRF = Arrays.copyOf(rf.getEva(), numAtt);
    double[] valuesR = Arrays.copyOf(relief.getEva(), numAtt);
    double[] valuesSE = Arrays.copyOf(se.getEva(), numAtt);
    double[] valuesC = new double[numAtt];

    double sumRF = 0.0;

    for (int i = 0; i < numAtt; i++) {
      if (i != idx) {
        valuesC[i] = Math.abs(corr.evaluate(i));
        sumRF += valuesRF[i];
      }
    }

    if (sumRF == 0) {
      sumRF = 1;
    }
    double minRe = -1;
    double maxRe = 1;

    for (int i = 0; i < numAtt; i++) {
      if (i != idx) {
        valuesR[i] = (valuesR[i] - minRe) / (maxRe - minRe);
        valuesRF[i] = valuesRF[i] / sumRF;
      }
    }

    // evaluting for each MD usin min max noramlizacion en RF
    try {
      AFuzzyIntegral integral = FuzzyIntegralFactory.getFuzzyIntegral(confFuzzy);
      eva = new double[numAtt];
      for (int i = 0; i < numAtt; i++) {
        if (i == idx) {
          eva[i] = 0;
        } else {
          eva[i] =
              integral.compute(new double[] {valuesRF[i], valuesR[i], valuesSE[i], valuesC[i]});
        }
        data.setEva4DescPos(i, eva[i]);
      }
    } catch (Exception ex) {
      throw AExOpDCSException.ExceptionType.MD_EVALUATION_FUNCTION_EXCEPTION.get(
          "Problems computing choquet integral", ex);
    }
  }

  @Override
  public void setOptions(String[] opts) throws AExOpDCSException {
    try {
      Map<String, String> optsValues = ParseOptions.getOption(opts);
      String mType = optsValues.get("-m");
      String iType = optsValues.get("-i");
      String[] omOptions = optsValues.get("-mo").split("/");
      confFuzzy =
          new FuzzyIntegralConf(
              FuzzyIntegralType.getEnum(iType),
              new FuzzyMeasureConf(FuzzyMeasureType.getEnum(mType), omOptions));
    } catch (Exception ex) {
      throw AExOpDCSException.ExceptionType.PARSE_EXCEPTION.get("Error getting choquet options");
    }
  }

  @Override
  public String[] getOptions() {
    throw new UnsupportedOperationException(
        "Not supported yet."); // To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public AttributeEvaluationType getType() {
    return AttributeEvaluationType.CHOQUET;
  }
}
