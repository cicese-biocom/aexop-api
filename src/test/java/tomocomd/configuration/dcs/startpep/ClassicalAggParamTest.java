package tomocomd.configuration.dcs.startpep;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ClassicalAggParamTest {

  ClassicalAggParam classicalAggParam;

  @BeforeEach
  void setUp() {
    classicalAggParam = new ClassicalAggParam();
  }

  @Test
  void setDefault() {
    assertArrayEquals(getDefaultValues(), classicalAggParam.getValues());
  }

  @Test
  void getValues() {
    ClassicalAggParam classics = new ClassicalAggParam(classicalAggParam);
    classics.setValue("AC", false);
    assertArrayEquals(getDefaultValuesWithOutAC(), classics.getValues());
  }

  @Test
  void getValuesFalse() {
    classicalAggParam.setValue("MIC", false);
    classicalAggParam.setValue("", false);
    classicalAggParam.setValue("GV", false);
    classicalAggParam.setValue("ES", false);
    classicalAggParam.setValue("TS", false);
    assertArrayEquals(getDefaultValuesWithAC(), classicalAggParam.getValues());
  }

  @Test
  void testValidate() {
    Map<String, Boolean> params = classicalAggParam.getParams();
    params.keySet().forEach(key -> classicalAggParam.setValue(key, false));
    classicalAggParam.validate();
    assertArrayEquals(getDefaultValues(), classicalAggParam.getValues());
  }

  @Test
  void getParamName() {
    assertEquals("Classical aggregators", classicalAggParam.getParamName());
  }

  @Test
  void testToString() {
    assertEquals(
        "Classical Aggregation Operator=[MIC,ES,,AC[1],AC[2],AC[3],AC[4],AC[5],AC[6],AC[7],GV[1],GV[2],GV[3],GV[4],GV[5],GV[6],GV[7],TS[1],TS[2],TS[3],TS[4],TS[5],TS[6],TS[7]]",
        classicalAggParam.toString());
  }

  String[] getDefaultValues() {
    return new String[] {
      "MIC", "ES", "", "AC[1]", "AC[2]", "AC[3]", "AC[4]", "AC[5]", "AC[6]", "AC[7]", "GV[1]",
      "GV[2]", "GV[3]", "GV[4]", "GV[5]", "GV[6]", "GV[7]", "TS[1]", "TS[2]", "TS[3]", "TS[4]",
      "TS[5]", "TS[6]", "TS[7]"
    };
  }

  String[] getDefaultValuesWithAC() {
    return new String[] {"AC[1]", "AC[2]", "AC[3]", "AC[4]", "AC[5]", "AC[6]", "AC[7]"};
  }

  String[] getDefaultValuesWithOutAC() {
    return new String[] {
      "MIC", "ES", "", "GV[1]", "GV[2]", "GV[3]", "GV[4]", "GV[5]", "GV[6]", "GV[7]", "TS[1]",
      "TS[2]", "TS[3]", "TS[4]", "TS[5]", "TS[6]", "TS[7]"
    };
  }
}
