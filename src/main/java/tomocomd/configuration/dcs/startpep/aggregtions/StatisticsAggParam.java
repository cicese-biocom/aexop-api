package tomocomd.configuration.dcs.startpep.aggregtions;

import tomocomd.configuration.dcs.AParamsDCS;

public class StatisticsAggParam extends AParamsDCS {

  public StatisticsAggParam() {
    super();
  }

  public StatisticsAggParam(StatisticsAggParam paramsHead) {
    super(paramsHead);
  }

  @Override
  public void setDefault() {
    params.put("V", true);
    params.put("S", true);
    params.put("K", true);
    params.put("SD", true);
    params.put("VC", true);
    params.put("RA", true);
    params.put("Q1", true);
    params.put("Q2", true);
    params.put("Q3", true);
    params.put("I50", true);
    params.put("MX", true);
    params.put("MN", true);
  }

  @Override
  public void validate() {
    // this allows all false
  }

  @Override
  public String getParamName() {
    return "Statistics aggregators";
  }
}
