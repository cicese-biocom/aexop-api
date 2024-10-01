package tomocomd.configuration.dcs.startpep.aggregtions;

import tomocomd.configuration.dcs.AParamsDCS;

public class GowawaAggParam extends AParamsDCS {

  public GowawaAggParam() {
    super();
  }

  public GowawaAggParam(GowawaAggParam paramsHead) {
    super(paramsHead);
  }

  @Override
  public void setDefault() {
    params.put("GOWAWA", true);
  }

  @Override
  public String[] getValues() {
    if (Boolean.TRUE.equals(params.get("GOWAWA"))) return defaultOWAWAs;
    else return new String[] {};
  }

  @Override
  public String getParamName() {
    return "Gowawa aggregators";
  }

  @Override
  public void validate() {}

  private static final String[] defaultOWAWAs = {
    "GOWAWA[0.9;1;AO2-OWA;1.0;0.0;1;ES2-OWA;0.9;0.0]",
    "GOWAWA[1.0;1;AO2-OWA;1.0;0.0;1;NONE;0.0;0.0]",
    "GOWAWA[0.1;1;S-OWA;1.0;0.0;1;ES2-OWA;0.9;0.0]",
    "GOWAWA[0.2;1;S-OWA;1.0;0.0;1;ES2-OWA;0.9;0.0]",
    "GOWAWA[0.7;1;S-OWA;0.8;0.2;1;ES2-OWA;0.9;0.0]",
    "GOWAWA[0.8;1;S-OWA;1.0;0.0;1;ES2-OWA;0.9;0.0]",
    "GOWAWA[0.5;1;ES1-OWA;0.7;0.0;1;ES2-OWA;0.9;0.0]",
    "GOWAWA[0.2;1;AO2-OWA;1.0;0.0;1;ES2-OWA;0.9;0.0]",
    "GOWAWA[1.0;1;ES1-OWA;0.7;0.0;1;NONE;0.0;0.0]",
    "GOWAWA[0.5;1;S-OWA;1.0;0.0;1;ES2-OWA;0.9;0.0]",
    "GOWAWA[0.9;1;ES1-OWA;0.7;0.0;1;ES2-OWA;0.9;0.0]",
    "GOWAWA[0.2;1;ES1-OWA;0.7;0.0;1;ES2-OWA;0.9;0.0]",
    "GOWAWA[0.7;1;ES1-OWA;0.7;0.0;1;ES2-OWA;0.9;0.0]",
    "GOWAWA[0.6;1;AO2-OWA;1.0;0.0;1;ES2-OWA;0.9;0.0]",
    "GOWAWA[0.5;1;AO2-OWA;1.0;0.0;1;ES2-OWA;0.9;0.0]",
    "GOWAWA[1.0;1;S-OWA;0.8;0.2;1;NONE;0.0;0.0]",
    "GOWAWA[0.7;1;AO2-OWA;1.0;0.0;1;ES2-OWA;0.9;0.0]",
    "GOWAWA[0.0;1;NONE;0.0;0.0;1;S-OWA;1.0;0.0]",
    "GOWAWA[0.1;0;W-OWA;0.1;0.6;2;W-OWA;0.1;0.2]",
    "GOWAWA[0.0;1;NONE;0.0;0.0;2;W-OWA;0.4;0.5]",
    "GOWAWA[0.0;1;NONE;0.0;0.0;2;W-OWA;0.7;0.8]",
    "GOWAWA[0.1;2;ES2-OWA;0.9;0.0;2;W-OWA;0.3;0.4]",
    "GOWAWA[0.3;1;S-OWA;1.0;0.0;1;ES2-OWA;0.9;0.0]",
    "GOWAWA[0.4;1;ES1-OWA;0.7;0.0;1;ES2-OWA;0.9;0.0]",
    "GOWAWA[0.9;1;S-OWA;1.0;0.0;1;ES2-OWA;0.9;0.0]",
    "GOWAWA[0.0;1;NONE;0.0;0.0;2;W-OWA;0.5;0.6]",
    "GOWAWA[0.1;0;AO1-OWA;1.0;0.0;2;S-OWA;0.8;0.1]",
    "GOWAWA[0.0;1;NONE;0.0;0.0;2;W-OWA;0.5;0.7]",
    "GOWAWA[0.6;1;S-OWA;1.0;0.0;2;W-OWA;0.9;1.0]",
    "GOWAWA[0.1;2;ES2-OWA;0.9;0.0;2;W-OWA;0.2;0.3]",
    "GOWAWA[1.0;1;ES1-OWA;0.7;0.0;1;NONE;0.0;0.0]",
    "GOWAWA[1.0;1;S-OWA;1.0;0.0;1;NONE;0.0;0.0]",
    "GOWAWA[1.0;2;ES2-OWA;0.9;0.0;1;NONE;0.0;0.0]",
    "GOWAWA[0.5;1;AO2-OWA;1.0;0.0;1;ES2-OWA;0.9;0.0]",
    "GOWAWA[1.0;1;S-OWA;0.8;0.2;1;NONE;0.0;0.0]",
    "GOWAWA[0.0;1;NONE;0.0;0.0;0;S-OWA;0.0;1.0]",
    "GOWAWA[0.7;2;ES2-OWA;0.9;0.0;0;S-OWA;0.0;1.0]",
    "GOWAWA[0.1;2;ES2-OWA;0.9;0.0;2;W-OWA;0.7;0.8]",
    "GOWAWA[0.1;2;ES2-OWA;0.9;0.0;0;W-OWA;0.0;0.1]",
    "GOWAWA[0.9;1;AO2-OWA;1.0;0.0;1;ES2-OWA;0.9;0.0]",
    "GOWAWA[0.2;2;ES2-OWA;0.9;0.0;2;W-OWA;0.4;0.6]",
    "GOWAWA[0.3;2;S-OWA;0.6;0.0;2;W-OWA;0.9;1.0]",
    "GOWAWA[0.4;1;AO2-OWA;1.0;0.0;1;ES2-OWA;0.9;0.0]",
    "GOWAWA[1.0;1;AO2-OWA;1.0;0.0;1;NONE;0.0;0.0]",
    "GOWAWA[0.0;1;NONE;0.0;0.0;1;S-OWA;1.0;0.0]",
    "GOWAWA[0.8;1;AO2-OWA;1.0;0.0;1;ES2-OWA;0.9;0.0]",
    "GOWAWA[0.9;1;ES1-OWA;0.7;0.0;1;ES2-OWA;0.9;0.0]",
    "GOWAWA[0.2;2;S-OWA;0.6;0.0;2;S-OWA;0.8;0.1]"
  };
}
