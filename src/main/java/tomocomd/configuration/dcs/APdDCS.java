package tomocomd.configuration.dcs;

import java.io.Serializable;

public abstract class APdDCS implements Serializable {

  protected final String name;

  protected APdDCS() {
    this.name = "PD";
  }

  protected APdDCS(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public abstract PDType getType();

  public abstract AHeadEntity randomHeading();

  public abstract long getSetDim();

  public abstract String getDesc();

  public abstract String[] getValues4Param(String paramName);
}
