package tomocomd.configuration.dcs.startpep;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tomocomd.exceptions.AExOpDCSException;

class GroupsParamTest {

  GroupsParam groupsParam;

  @BeforeEach
  void setUp() {
    groupsParam = new GroupsParam();
  }

  @Test
  void setDefault() {
    String[] values = groupsParam.getValues();
    assertArrayEquals(getGroups(), values);
  }

  @Test
  void setValue() {
    groupsParam.setValue("T", false);
    assertArrayEquals(getGroupsWithoutT(), groupsParam.getValues());
  }

  @Test
  void setWrongValue() {
    GroupsParam groups = new GroupsParam(groupsParam);
    assertThrows(AExOpDCSException.class, () -> groups.setValue("T1", false));
  }

  @Test
  void getValue() {
    groupsParam.setValue("T", false);
    assertFalse(groupsParam.getValue("T"));
  }

  @Test
  void getWrongValue() {
    assertThrows(AExOpDCSException.class, () -> groupsParam.getValue("T1"));
  }

  @Test
  void testValidate() {
    Map<String, Boolean> params = groupsParam.getParams();
    params.keySet().forEach(key -> groupsParam.setValue(key, false));
    groupsParam.validate();
    assertArrayEquals(getGroups(), groupsParam.getValues());
  }

  @Test
  void testToString() {
    assertEquals("Chemical Groups=[T,A,H,P,R,B,F,N,C,U,D]", groupsParam.toString());
  }

  @Test
  void getParamName() {
    assertEquals("Chemical Groups", groupsParam.getParamName());
  }

  String[] getGroups() {
    return new String[] {"T", "A", "H", "P", "R", "B", "F", "N", "C", "U", "D"};
  }

  String[] getGroupsWithoutT() {
    return new String[] {"A", "H", "P", "R", "B", "F", "N", "C", "U", "D"};
  }
}
