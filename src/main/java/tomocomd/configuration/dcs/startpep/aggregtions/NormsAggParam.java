package tomocomd.configuration.dcs.startpep.aggregtions;

import tomocomd.configuration.dcs.AParamsDCS;

public class NormsAggParam extends AParamsDCS {

  public NormsAggParam() {
    super();
  }

  public NormsAggParam(NormsAggParam paramsHead) {
    super(paramsHead);
  }

  @Override
  public void setDefault() {
    params.put("N1", true);
    params.put("N2", true);
    params.put("N3", true);
  }

  @Override
  public void validate() {
    // this allows all false
  }

  @Override
  public String getParamName() {
    return "Norms aggregators";
  }
}
