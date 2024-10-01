package tomocomd.configuration.dcs.startpep.aggregtions;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GowawaAggParamTest {

  GowawaAggParam gowawaAggParam;

  @BeforeEach
  void setUp() {
    gowawaAggParam = new GowawaAggParam();
  }

  @Test
  void setDefault() {
    assertTrue(gowawaAggParam.getValue("GOWAWA"));
  }

  @Test
  void getValues() {
    assertArrayEquals(defaultOWAWAs, gowawaAggParam.getValues());
  }

  @Test
  void getParamName() {
    assertEquals("Gowawa aggregators", gowawaAggParam.getParamName());
  }

  @Test
  void validate() {
    GowawaAggParam gowawa = new GowawaAggParam(gowawaAggParam);
    gowawa.setValue("GOWAWA", false);
    String[] values = gowawa.getValues();
    assertTrue(Objects.nonNull(values) && values.length == 0);
  }

  private static final String[] defaultOWAWAs = {
    "GOWAWA[0.9;1;AO2-OWA;1.0;0.0;1;ES2-OWA;0.9;0.0]",
    "GOWAWA[1.0;1;AO2-OWA;1.0;0.0;1;NONE;0.0;0.0]",
    "GOWAWA[0.1;1;S-OWA;1.0;0.0;1;ES2-OWA;0.9;0.0]",
    "GOWAWA[0.2;1;S-OWA;1.0;0.0;1;ES2-OWA;0.9;0.0]",
    "GOWAWA[0.7;1;S-OWA;0.8;0.2;1;ES2-OWA;0.9;0.0]",
    "GOWAWA[0.8;1;S-OWA;1.0;0.0;1;ES2-OWA;0.9;0.0]",
    "GOWAWA[0.5;1;ES1-OWA;0.7;0.0;1;ES2-OWA;0.9;0.0]",
    "GOWAWA[0.2;1;AO2-OWA;1.0;0.0;1;ES2-OWA;0.9;0.0]",
    "GOWAWA[1.0;1;ES1-OWA;0.7;0.0;1;NONE;0.0;0.0]",
    "GOWAWA[0.5;1;S-OWA;1.0;0.0;1;ES2-OWA;0.9;0.0]",
    "GOWAWA[0.9;1;ES1-OWA;0.7;0.0;1;ES2-OWA;0.9;0.0]",
    "GOWAWA[0.2;1;ES1-OWA;0.7;0.0;1;ES2-OWA;0.9;0.0]",
    "GOWAWA[0.7;1;ES1-OWA;0.7;0.0;1;ES2-OWA;0.9;0.0]",
    "GOWAWA[0.6;1;AO2-OWA;1.0;0.0;1;ES2-OWA;0.9;0.0]",
    "GOWAWA[0.5;1;AO2-OWA;1.0;0.0;1;ES2-OWA;0.9;0.0]",
    "GOWAWA[1.0;1;S-OWA;0.8;0.2;1;NONE;0.0;0.0]",
    "GOWAWA[0.7;1;AO2-OWA;1.0;0.0;1;ES2-OWA;0.9;0.0]",
    "GOWAWA[0.0;1;NONE;0.0;0.0;1;S-OWA;1.0;0.0]",
    "GOWAWA[0.1;0;W-OWA;0.1;0.6;2;W-OWA;0.1;0.2]",
    "GOWAWA[0.0;1;NONE;0.0;0.0;2;W-OWA;0.4;0.5]",
    "GOWAWA[0.0;1;NONE;0.0;0.0;2;W-OWA;0.7;0.8]",
    "GOWAWA[0.1;2;ES2-OWA;0.9;0.0;2;W-OWA;0.3;0.4]",
    "GOWAWA[0.3;1;S-OWA;1.0;0.0;1;ES2-OWA;0.9;0.0]",
    "GOWAWA[0.4;1;ES1-OWA;0.7;0.0;1;ES2-OWA;0.9;0.0]",
    "GOWAWA[0.9;1;S-OWA;1.0;0.0;1;ES2-OWA;0.9;0.0]",
    "GOWAWA[0.0;1;NONE;0.0;0.0;2;W-OWA;0.5;0.6]",
    "GOWAWA[0.1;0;AO1-OWA;1.0;0.0;2;S-OWA;0.8;0.1]",
    "GOWAWA[0.0;1;NONE;0.0;0.0;2;W-OWA;0.5;0.7]",
    "GOWAWA[0.6;1;S-OWA;1.0;0.0;2;W-OWA;0.9;1.0]",
    "GOWAWA[0.1;2;ES2-OWA;0.9;0.0;2;W-OWA;0.2;0.3]",
    "GOWAWA[1.0;1;ES1-OWA;0.7;0.0;1;NONE;0.0;0.0]",
    "GOWAWA[1.0;1;S-OWA;1.0;0.0;1;NONE;0.0;0.0]",
    "GOWAWA[1.0;2;ES2-OWA;0.9;0.0;1;NONE;0.0;0.0]",
    "GOWAWA[0.5;1;AO2-OWA;1.0;0.0;1;ES2-OWA;0.9;0.0]",
    "GOWAWA[1.0;1;S-OWA;0.8;0.2;1;NONE;0.0;0.0]",
    "GOWAWA[0.0;1;NONE;0.0;0.0;0;S-OWA;0.0;1.0]",
    "GOWAWA[0.7;2;ES2-OWA;0.9;0.0;0;S-OWA;0.0;1.0]",
    "GOWAWA[0.1;2;ES2-OWA;0.9;0.0;2;W-OWA;0.7;0.8]",
    "GOWAWA[0.1;2;ES2-OWA;0.9;0.0;0;W-OWA;0.0;0.1]",
    "GOWAWA[0.9;1;AO2-OWA;1.0;0.0;1;ES2-OWA;0.9;0.0]",
    "GOWAWA[0.2;2;ES2-OWA;0.9;0.0;2;W-OWA;0.4;0.6]",
    "GOWAWA[0.3;2;S-OWA;0.6;0.0;2;W-OWA;0.9;1.0]",
    "GOWAWA[0.4;1;AO2-OWA;1.0;0.0;1;ES2-OWA;0.9;0.0]",
    "GOWAWA[1.0;1;AO2-OWA;1.0;0.0;1;NONE;0.0;0.0]",
    "GOWAWA[0.0;1;NONE;0.0;0.0;1;S-OWA;1.0;0.0]",
    "GOWAWA[0.8;1;AO2-OWA;1.0;0.0;1;ES2-OWA;0.9;0.0]",
    "GOWAWA[0.9;1;ES1-OWA;0.7;0.0;1;ES2-OWA;0.9;0.0]",
    "GOWAWA[0.2;2;S-OWA;0.6;0.0;2;S-OWA;0.8;0.1]"
  };
}
