package tomocomd.configuration.dcs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, property = "type")
public abstract class AAttributeDCS implements Serializable {

  protected final String name;

  @JsonIgnore
  public abstract AttributeType getType();

  public abstract AHeadEntity randomHeading();

  @JsonIgnore
  public abstract String getDesc();

  public abstract String[] getValues4Param(String paramName);
}
