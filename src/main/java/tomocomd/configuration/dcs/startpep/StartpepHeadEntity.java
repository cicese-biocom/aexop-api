package tomocomd.configuration.dcs.startpep;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import tomocomd.configuration.dcs.AHeadEntity;
import tomocomd.configuration.dcs.PDType;
import tomocomd.exceptions.AExOpDCSException;
import tomocomd.md.HeaderValidator;
import tomocomd.utils.Constants;

@Builder
@AllArgsConstructor
public class StartpepHeadEntity implements AHeadEntity {

  private String agregOpeClas;
  private String agreOpe;
  private String group;
  private String prop;
  @Getter private final PDType type;

  public StartpepHeadEntity(PDType type) {
    this.type = type;
    this.agregOpeClas = "";
    this.agreOpe = "";
    this.group = "";
    this.prop = "";
  }

  @Override
  public Map<String, String> parseHead2Map() {
    return Map.of(
        Constants.CLASAGGOPECONST,
        agregOpeClas,
        Constants.AGGOPECONST,
        agreOpe,
        Constants.GROUPSCONST,
        group,
        Constants.PROPCONST,
        prop);
  }

  @Override
  public String toString() {
    String head = agregOpeClas.isEmpty() ? agreOpe : agregOpeClas + "_" + agreOpe;
    head += "_" + group;
    head += "_" + prop;
    return head;
  }

  @Override
  public void parseMap2Head(Map<String, String> head) throws AExOpDCSException {
    try {
      String headString =
          head.containsKey(Constants.CLASAGGOPECONST)
                  && !head.get(Constants.CLASAGGOPECONST).isEmpty()
              ? head.get(Constants.CLASAGGOPECONST) + "_" + head.get(Constants.AGGOPECONST)
              : head.get(Constants.AGGOPECONST);
      headString += "_" + head.get(Constants.GROUPSCONST);
      headString += "_" + head.get(Constants.PROPCONST);
      setFromString(headString);
    } catch (AExOpDCSException e) {
      throw e;
    } catch (Exception e) {
      throw AExOpDCSException.ExceptionType.DCS_EXCEPTION.get(
          "Error parsing head: " + e.getMessage());
    }
  }

  @Override
  public void setFromString(String cad) throws AExOpDCSException {
    if (!HeaderValidator.validateHeader(cad)) {
      throw AExOpDCSException.ExceptionType.DCS_EXCEPTION.get("Invalid head format: " + cad);
    }
    String[] parts = cad.split("_");

    agregOpeClas = parts.length == 3 ? "" : parts[0];
    agreOpe = parts.length == 3 ? parts[0] : parts[1];
    group = parts.length == 3 ? parts[1] : parts[2];
    prop = parts.length == 3 ? parts[2] : parts[3];
  }

  @Override
  public boolean setParamValue(String paramName, String value) throws AExOpDCSException {
    String old;
    switch (paramName) {
      case Constants.CLASAGGOPECONST:
        old = agregOpeClas;
        agregOpeClas = value;
        if (!HeaderValidator.validateHeader(toString())) {
          agregOpeClas = old;
          return false;
        }
        return true;
      case Constants.AGGOPECONST:
        old = agreOpe;
        agreOpe = value;
        if (!HeaderValidator.validateHeader(toString())) {
          agreOpe = old;
          return false;
        }
        return true;
      case Constants.GROUPSCONST:
        old = group;
        group = value;
        if (!HeaderValidator.validateHeader(toString())) {
          group = old;
          return false;
        }
        return true;
      case Constants.PROPCONST:
        old = prop;
        prop = value;
        if (!HeaderValidator.validateHeader(toString())) {
          prop = old;
          return false;
        }
        return true;
      default:
        return false;
    }
  }
}
