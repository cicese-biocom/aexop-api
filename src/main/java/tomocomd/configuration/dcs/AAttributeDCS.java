package tomocomd.configuration.dcs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import tomocomd.configuration.dcs.startpep.StartpepDCS;

@Data
@AllArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({@JsonSubTypes.Type(value = StartpepDCS.class, name = "StartpepDCS")})
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class AAttributeDCS implements Serializable {

  protected final String name;

  protected AAttributeDCS() {
    this.name = "PD";
  }

  @JsonIgnore
  public abstract PDType getType();

  public abstract AHeadEntity randomHeading();

  @JsonIgnore
  public abstract long getSetDim();

  @JsonIgnore
  public abstract String getDesc();

  public abstract String[] getValues4Param(String paramName);
}
