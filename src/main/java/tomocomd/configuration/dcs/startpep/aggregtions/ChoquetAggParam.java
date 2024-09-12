package tomocomd.configuration.dcs.startpep.aggregtions;

import tomocomd.configuration.dcs.AParamsDCS;

public class ChoquetAggParam extends AParamsDCS {

  public ChoquetAggParam() {
    super();
  }

  public ChoquetAggParam(ChoquetAggParam paramsHead) {
    super(paramsHead);
  }

  @Override
  public void setDefault() {
    params.put("CHOQUET", true);
  }

  @Override
  public String[] getValues() {
    if (Boolean.TRUE.equals(params.get("CHOQUET"))) return defaultChoquet;
    else return new String[] {};
  }

  @Override
  public String getParamName() {
    return "Choquet aggregators";
  }

  @Override
  public void validate() {}

  private static final String[] defaultChoquet = {
    "CHOQUET[A;-0.75;AO2;0.6]",
    "CHOQUET[A;-0.75;AO1;0.3]",
    "CHOQUET[A;-0.25;AO2;0.6]",
    "CHOQUET[A;0.75;AO2;0.6]",
    "CHOQUET[A;0.75;AO1;0.2]",
    "CHOQUET[A;0.25;AO1;0.9]",
    "CHOQUET[A;0.25;AO2;0.6]",
    "CHOQUET[A;0.5;AO1;0.2]",
    "CHOQUET[A;0.75;AO2;0.5]",
    "CHOQUET[A;0.75;AO1;0.8]",
    "CHOQUET[A;0.5;AO1;0.9]",
    "CHOQUET[A;0.25;AO1;0.8]",
    "CHOQUET[A;-0.25;AO2;1.0]",
    "CHOQUET[A;-0.25;AO2;0.8]",
    "CHOQUET[A;0.5;AO2;0.6]",
    "CHOQUET[A;-0.25;AO1;0.8]",
    "CHOQUET[A;-0.75;AO1;0.9]",
    "CHOQUET[A;0.5;AO2;0.9]",
    "CHOQUET[A;0.75;AO1;0.9]",
    "CHOQUET[A;-0.75;AO2;0.9]",
    "CHOQUET[A;-0.75;AO1;1.0]",
    "CHOQUET[A;0.5;AO2;0.8]",
    "CHOQUET[A;0.25;AO2;0.5]",
    "CHOQUET[A;-0.75;AO2;0.7]",
    "CHOQUET[A;-0.75;AO2;1.0]",
    "CHOQUET[A;-0.5;AO1;0.3]",
    "CHOQUET[A;-0.75;AO2;0.0]",
    "CHOQUET[A;-0.75;AO1;0.2]",
    "CHOQUET[A;-0.5;AO2;0.0]",
    "CHOQUET[A;0.5;AO1;0.8]",
    "CHOQUET[A;0.25;AO1;0.2]",
    "CHOQUET[A;-0.5;AO1;0.2]",
    "CHOQUET[A;0.5;AO2;0.5]",
    "CHOQUET[D;-0.75;AO2;0.6]",
    "CHOQUET[D;-0.75;AO1;0.3]",
    "CHOQUET[D;-0.25;AO2;0.6]",
    "CHOQUET[D;0.75;AO2;0.6]",
    "CHOQUET[D;0.75;AO1;0.2]",
    "CHOQUET[D;0.25;AO1;0.9]",
    "CHOQUET[D;0.25;AO2;0.6]",
    "CHOQUET[D;0.5;AO1;0.2]",
    "CHOQUET[D;0.75;AO2;0.5]",
    "CHOQUET[D;0.75;AO1;0.8]",
    "CHOQUET[D;0.5;AO1;0.9]",
    "CHOQUET[D;0.25;AO1;0.8]",
    "CHOQUET[D;-0.25;AO2;1.0]",
    "CHOQUET[D;-0.25;AO2;0.8]",
    "CHOQUET[D;0.5;AO2;0.6]",
    "CHOQUET[D;-0.25;AO1;0.8]",
    "CHOQUET[D;-0.75;AO1;0.9]",
    "CHOQUET[D;0.5;AO2;0.9]",
    "CHOQUET[D;0.75;AO1;0.9]",
    "CHOQUET[D;-0.75;AO2;0.9]",
    "CHOQUET[D;-0.75;AO1;1.0]",
    "CHOQUET[D;0.5;AO2;0.8]",
    "CHOQUET[D;0.25;AO2;0.5]",
    "CHOQUET[D;-0.75;AO2;0.7]",
    "CHOQUET[D;-0.75;AO2;1.0]",
    "CHOQUET[D;-0.5;AO1;0.3]",
    "CHOQUET[D;-0.75;AO2;0.0]",
    "CHOQUET[D;-0.75;AO1;0.2]",
    "CHOQUET[D;-0.5;AO2;0.0]",
    "CHOQUET[D;0.5;AO1;0.8]",
    "CHOQUET[D;0.25;AO1;0.2]",
    "CHOQUET[D;-0.5;AO1;0.2]",
    "CHOQUET[D;0.5;AO2;0.5]"
  };
}
