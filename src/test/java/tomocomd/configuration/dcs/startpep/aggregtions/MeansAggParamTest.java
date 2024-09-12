package tomocomd.configuration.dcs.startpep.aggregtions;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MeansAggParamTest {

  MeansAggParam meansAggParam;

  @BeforeEach
  void setUp() {
    meansAggParam = new MeansAggParam();
  }

  @Test
  void setDefault() {
    assertArrayEquals(getDefaults(), meansAggParam.getValues());
  }

  @Test
  void getParamName() {
    assertEquals("Means aggregators", meansAggParam.getParamName());
  }

  @Test
  void testValidate() {
    MeansAggParam means = new MeansAggParam(meansAggParam);
    means.getParams().keySet().forEach(key -> means.setValue(key, false));
    String[] values = means.getValues();
    assertTrue(Objects.nonNull(values) && values.length == 0);
  }

  String[] getDefaults() {
    return new String[] {"GM", "AM", "P2", "P3", "HM"};
  }
}
