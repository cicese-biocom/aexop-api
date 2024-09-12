package tomocomd.configuration.dcs.startpep.aggregtions;

import tomocomd.configuration.dcs.AParamsDCS;

public class InformationAggParam extends AParamsDCS {

  public InformationAggParam() {
    super();
  }

  public InformationAggParam(InformationAggParam paramsHead) {
    super(paramsHead);
  }

  @Override
  public void validate() {}

  @Override
  public void setDefault() {
    params.put("TIC", true);
    params.put("SIC", true);
  }

  @Override
  public String getParamName() {
    return "Information aggregators";
  }
}
