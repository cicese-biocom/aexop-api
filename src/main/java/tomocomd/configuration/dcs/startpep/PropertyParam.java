package tomocomd.configuration.dcs.startpep;

import tomocomd.configuration.dcs.AParamsDCS;

public final class PropertyParam extends AParamsDCS {

  public PropertyParam() {
    super();
  }

  public PropertyParam(PropertyParam paramsHead) {
    super(paramsHead);
  }

  @Override
  public void setDefault() {
    params.put("ptt", true);
    params.put("gcp1", true);
    params.put("gcp2", true);
    params.put("eps", true);
    params.put("scm", true);
    params.put("scv", true);
    params.put("pie", true);
    params.put("pah", true);
    params.put("pbs", true);
    params.put("isa", true);
    params.put("z1", true);
    params.put("z2", true);
    params.put("z3", true);
    params.put("mw", true);
    params.put("bi", true);
    params.put("khh", true);
    params.put("hwhh", true);
    params.put("cch", true);
    params.put("kh", true);
    params.put("cdch", true);
  }

  @Override
  public String getParamName() {
    return "Aminoacid properties";
  }
}
