package tomocomd.configuration.dcs.startpep;

import static org.junit.jupiter.api.Assertions.*;

import lombok.SneakyThrows;
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
  void getValuesFalse() {
    classicalAggParam.setValue("AC", true);
    classicalAggParam.setValue("", false);
    assertArrayEquals(getDefaultValuesWithAC(), classicalAggParam.getValues());
  }

  @Test
  void testValidate() {
    classicalAggParam.validate();
    assertArrayEquals(getDefaultValues(), classicalAggParam.getValues());
  }

  @Test
  void getParamName() {
    assertEquals("Classical aggregators", classicalAggParam.getParamName());
  }

  @Test
  @SneakyThrows
  void testToString() {
    for (String key : classicalAggParam.getParams().keySet()) classicalAggParam.setValue(key, true);
    assertEquals(
        "Classical Aggregation Operator=[MIC,ES,,AC[1],AC[2],AC[3],AC[4],AC[5],AC[6],AC[7],GV[1],GV[2],GV[3],GV[4],GV[5],GV[6],GV[7],TS[1],TS[2],TS[3],TS[4],TS[5],TS[6],TS[7]]",
        classicalAggParam.toString());
  }

  String[] getDefaultValues() {
    return new String[] {""};
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
