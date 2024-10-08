package tomocomd.configuration.dcs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tomocomd.exceptions.AExOpDCSException;

@Data
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class AParamsDCS implements Serializable {

  private static final Logger LOGGER = LoggerFactory.getLogger(AParamsDCS.class);

  protected Map<String, Boolean> params;

  protected AParamsDCS() {
    init();
    setDefault();
  }

  public void init() {
    this.params = new LinkedHashMap<>();
  }

  protected AParamsDCS(AParamsDCS paramsHead) {
    this.params = new LinkedHashMap<>(paramsHead.getParams());
    validate();
  }

  public void setValue(String key, Boolean value) throws AExOpDCSException {
    if (params.containsKey(key)) {
      params.put(key, value);
    } else {
      throw AExOpDCSException.ExceptionType.MD_PARAM_EXCEPTION_TYPE.get(
          String.format("Invalid param name %s for param %s", key, getParamName()));
    }
    validate();
  }

  public Boolean getValue(String key) throws AExOpDCSException {
    if (params.containsKey(key)) {
      return params.get(key);
    } else {
      throw AExOpDCSException.ExceptionType.MD_PARAM_EXCEPTION_TYPE.get(
          String.format("Invalid param name %s for param %s", key, getParamName()));
    }
  }

  @JsonIgnore
  public String[] getValues() {
    return params.entrySet().stream()
        .filter(Map.Entry::getValue)
        .map(Map.Entry::getKey)
        .toArray(String[]::new);
  }

  public abstract void setDefault();

  public void validate() {
    Boolean flg =
        params.values().stream()
            .map(Boolean.class::cast)
            .reduce(Boolean::logicalOr)
            .orElse(Boolean.FALSE);

    if (Boolean.FALSE.equals(flg)) {
      setDefault();
      LOGGER.warn("Invalid {} configuration, set it to default configuration!!!", getParamName());
    }
  }

  public abstract String getParamName();

  @Override
  public String toString() {
    return getParamName()
        + "="
        + params.entrySet().stream()
            .filter(Map.Entry::getValue)
            .map(Map.Entry::getKey)
            .collect(Collectors.joining(",", "[", "]"));
  }
}
