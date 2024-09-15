package tomocomd.subsetsearch.evaluation.subsetevaluation;

import tomocomd.configuration.evaluation.subsetevaluation.SubsetEvaluationConfig;
import tomocomd.configuration.evaluation.subsetevaluation.subsetsearch.SubsetSearchType;
import tomocomd.exceptions.AExOpDCSException;

public class EvaluateSubsetFactory {
  private EvaluateSubsetFactory() {
    throw new IllegalStateException();
  }

  public static IEvaluateSubset getEvaluateSubsetMethod(SubsetEvaluationConfig conf)
      throws AExOpDCSException {

    if (conf.getSubsetSearchConf().getSubsetSearchType() == SubsetSearchType.MEAN_STD_REMOVE) {
      try {
        return new MeanPlusSTDRankingEvaluateSubset(conf);
      } catch (Exception ex) {
        throw AExOpDCSException.ExceptionType.SUBSET_EVALUATE_EXCEPTION.get(
            "Problems initializing Mean+STD evaluate subset", ex);
      }
    } else if (conf.getSubsetSearchConf().getSubsetSearchType() == SubsetSearchType.BEST_FIRST) {
      return new EvaluateSubset(conf);
    } else
      throw new IllegalStateException(
          String.format(
              "Search method %s type not defined",
              conf.getSubsetSearchConf().getSubsetSearchType()));
  }
}
