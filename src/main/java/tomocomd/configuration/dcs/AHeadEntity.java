package tomocomd.configuration.dcs;

import java.util.Map;
import tomocomd.exceptions.AExOpDCSException;

public interface AHeadEntity {

  Map<String, String> parseHead2Map();

  void parseMap2Head(Map<String, String> head) throws AExOpDCSException;

  void setFromString(String cad) throws AExOpDCSException;

  boolean setParamValue(String paramName, String value) throws AExOpDCSException;

  PDType getType();
}
