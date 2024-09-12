package tomocomd.configuration.dcs.startpep;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tomocomd.exceptions.AExOpDCSException;

class PropertyParamTest {

  PropertyParam propertyParam;

  @BeforeEach
  void setUp() {
    propertyParam = new PropertyParam();
  }

  @Test
  void setDefault() {
    String[] values = propertyParam.getValues();
    assertArrayEquals(getDefaultValues(), values);
  }

  @Test
  void setValue() {
    propertyParam.setValue("ptt", false);
    assertArrayEquals(getValuesWithOutPtt(), propertyParam.getValues());
  }

  @Test
  void getValue() {
    propertyParam.setValue("ptt", false);
    assertFalse(propertyParam.getValue("ptt"));
  }

  @Test
  void setWrongValue() {
    assertThrows(AExOpDCSException.class, () -> propertyParam.setValue("ptt1", false));
  }

  @Test
  void getWrongValue() {
    PropertyParam props = new PropertyParam(propertyParam);
    assertThrows(AExOpDCSException.class, () -> props.getValue("ptt1"));
  }

  @Test
  void getParamName() {
    assertEquals("Aminoacid properties", propertyParam.getParamName());
  }

  @Test
  void testValidate() {
    Map<String, Boolean> params = propertyParam.getParams();
    params.keySet().forEach(key -> propertyParam.setValue(key, false));
    propertyParam.validate();
    assertArrayEquals(getDefaultValues(), propertyParam.getValues());
  }

  @Test
  void testToString() {
    assertEquals(
        "Aminoacid properties=[ptt,gcp1,gcp2,eps,scm,scv,pie,pah,pbs,isa,z1,z2,z3,mw,bi,khh,hwhh,cch,kh,cdch]",
        propertyParam.toString());
  }

  String[] getDefaultValues() {
    return new String[] {
      "ptt", "gcp1", "gcp2", "eps", "scm", "scv", "pie", "pah", "pbs", "isa", "z1", "z2", "z3",
      "mw", "bi", "khh", "hwhh", "cch", "kh", "cdch"
    };
  }

  String[] getValuesWithOutPtt() {
    return new String[] {
      "gcp1", "gcp2", "eps", "scm", "scv", "pie", "pah", "pbs", "isa", "z1", "z2", "z3", "mw", "bi",
      "khh", "hwhh", "cch", "kh", "cdch"
    };
  }
}
