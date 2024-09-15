package tomocomd.configuration.dcs;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public abstract class AAttributeDCS implements Serializable {

  protected final String name;

  protected AAttributeDCS() {
    this.name = "PD";
  }

  public abstract PDType getType();

  public abstract AHeadEntity randomHeading();

  public abstract long getSetDim();

  public abstract String getDesc();

  public abstract String[] getValues4Param(String paramName);
}
