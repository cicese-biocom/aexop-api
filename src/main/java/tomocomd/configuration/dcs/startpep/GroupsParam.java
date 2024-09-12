package tomocomd.configuration.dcs.startpep;

import tomocomd.configuration.dcs.AParamsDCS;

public final class GroupsParam extends AParamsDCS {

  public GroupsParam() {
    super();
  }

  public GroupsParam(GroupsParam paramsHead) {
    super(paramsHead);
  }

  @Override
  public void setDefault() {
    params.put("T", true);
    params.put("A", true);
    params.put("H", true);
    params.put("P", true);
    params.put("R", true);
    params.put("B", true);
    params.put("F", true);
    params.put("N", true);
    params.put("C", true);
    params.put("U", true);
    params.put("D", true);
  }

  @Override
  public String getParamName() {
    return "Chemical Groups";
  }
}
