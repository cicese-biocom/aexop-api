package tomocomd.configuration.dcs.startpep;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tomocomd.configuration.dcs.PDType;
import tomocomd.exceptions.AExOpDCSException;
import tomocomd.utils.Constants;

class StartpepHeadEntityTest {

  StartpepHeadEntity startpepHeadEntity;

  @BeforeEach
  void setUp() {
    startpepHeadEntity =
        StartpepHeadEntity.builder()
            .type(PDType.STARTPEP)
            .prop("pie")
            .group("P")
            .agreOpe("N1")
            .agregOpeClas("AC[1]")
            .build();
  }

  @Test
  void parseHead2Map() {
    Map<String, String> map = startpepHeadEntity.parseHead2Map();
    assertEquals(4, map.size());
    assertEquals("AC[1]", map.get(Constants.CLASAGGOPECONST));
    assertEquals("N1", map.get(Constants.AGGOPECONST));
    assertEquals("P", map.get(Constants.GROUPSCONST));
    assertEquals("pie", map.get(Constants.PROPCONST));
  }

  @Test
  void testToString() {
    assertEquals("AC[1]_N1_P_pie", startpepHeadEntity.toString());
  }

  @Test
  void parseMap2Head() {
    Map<String, String> map =
        Map.of(
            Constants.CLASAGGOPECONST,
            "",
            Constants.AGGOPECONST,
            "N2",
            Constants.GROUPSCONST,
            "U",
            Constants.PROPCONST,
            "pie");
    startpepHeadEntity.parseMap2Head(map);
    assertEquals("N2_U_pie", startpepHeadEntity.toString());
  }

  @Test
  void parseMap2HeadFull() {
    Map<String, String> map =
        Map.of(
            Constants.CLASAGGOPECONST,
            "AC[1]",
            Constants.AGGOPECONST,
            "N2",
            Constants.GROUPSCONST,
            "U",
            Constants.PROPCONST,
            "pie");
    startpepHeadEntity.parseMap2Head(map);
    assertEquals("AC[1]_N2_U_pie", startpepHeadEntity.toString());
  }

  @Test
  void parseMap2HeadNotCLASAGGOPECONST() {
    Map<String, String> map =
        Map.of(Constants.AGGOPECONST, "N2", Constants.GROUPSCONST, "U", Constants.PROPCONST, "pie");
    startpepHeadEntity.parseMap2Head(map);
    assertEquals("N2_U_pie", startpepHeadEntity.toString());
  }

  @Test
  void parseMap2HeadWrongGroup() {
    Map<String, String> map =
        Map.of(
            Constants.CLASAGGOPECONST,
            "",
            Constants.AGGOPECONST,
            "N2",
            Constants.GROUPSCONST,
            "U2",
            Constants.PROPCONST,
            "pie");
    assertThrows(AExOpDCSException.class, () -> startpepHeadEntity.parseMap2Head(map));
  }

  @Test
  void parseMap2HeadNull() {
    assertThrows(AExOpDCSException.class, () -> startpepHeadEntity.parseMap2Head(null));
  }

  @Test
  void setParamValueClass() {
    assertTrue(startpepHeadEntity.setParamValue(Constants.CLASAGGOPECONST, "AC[2]"));
    assertEquals("AC[2]_N1_P_pie", startpepHeadEntity.toString());
  }

  @Test
  void setParamValueWrongClass() {
    assertFalse(startpepHeadEntity.setParamValue(Constants.CLASAGGOPECONST, "N2"));
    assertEquals("AC[1]_N1_P_pie", startpepHeadEntity.toString());
  }

  @Test
  void setParamValueAgg() {
    assertTrue(startpepHeadEntity.setParamValue(Constants.AGGOPECONST, "N2"));
    assertEquals("AC[1]_N2_P_pie", startpepHeadEntity.toString());
  }

  @Test
  void setParamValueWrongAgg() {
    assertFalse(startpepHeadEntity.setParamValue(Constants.AGGOPECONST, "N5"));
    assertEquals("AC[1]_N1_P_pie", startpepHeadEntity.toString());
  }

  @Test
  void setParamValueGroup() {
    assertTrue(startpepHeadEntity.setParamValue(Constants.GROUPSCONST, "T"));
    assertEquals("AC[1]_N1_T_pie", startpepHeadEntity.toString());
  }

  @Test
  void setParamValueWrongGroup() {
    assertFalse(startpepHeadEntity.setParamValue(Constants.GROUPSCONST, "N5"));
    assertEquals("AC[1]_N1_P_pie", startpepHeadEntity.toString());
  }

  @Test
  void setParamValueProp() {
    assertTrue(startpepHeadEntity.setParamValue(Constants.PROPCONST, "pah"));
    assertEquals("AC[1]_N1_P_pah", startpepHeadEntity.toString());
  }

  @Test
  void setParamValuePropWrongAtt() {
    assertFalse(startpepHeadEntity.setParamValue("as", "pah"));
    assertEquals("AC[1]_N1_P_pie", startpepHeadEntity.toString());
  }

  @Test
  void setParamValueWrongProp() {
    assertFalse(startpepHeadEntity.setParamValue(Constants.PROPCONST, "N5"));
    assertEquals("AC[1]_N1_P_pie", startpepHeadEntity.toString());
  }
}
