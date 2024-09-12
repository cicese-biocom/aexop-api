package tomocomd.configuration.dcs.startpep.aggregtions;

import tomocomd.configuration.dcs.AParamsDCS;

public class MeansAggParam extends AParamsDCS {

  public MeansAggParam() {
    super();
  }

  public MeansAggParam(MeansAggParam paramsHead) {
    super(paramsHead);
  }

  @Override
  public void setDefault() {
    params.put("GM", true);
    params.put("AM", true);
    params.put("P2", true);
    params.put("P3", true);
    params.put("HM", true);
  }

  @Override
  public void validate() {
    // this allows all false
  }

  @Override
  public String getParamName() {
    return "Means aggregators";
  }
}
