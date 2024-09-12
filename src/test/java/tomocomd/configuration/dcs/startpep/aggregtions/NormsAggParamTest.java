package tomocomd.configuration.dcs.startpep.aggregtions;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NormsAggParamTest {

  NormsAggParam normsAggParam;

  @BeforeEach
  void setUp() {
    normsAggParam = new NormsAggParam();
  }

  @Test
  void setDefault() {
    assertArrayEquals(getDefaults(), normsAggParam.getValues());
  }

  @Test
  void getParamName() {
    assertEquals("Norms aggregators", normsAggParam.getParamName());
  }

  @Test
  void testValidate() {
    NormsAggParam norms = new NormsAggParam(normsAggParam);
    norms.getParams().keySet().forEach(key -> norms.setValue(key, false));
    String[] values = norms.getValues();
    assertTrue(Objects.nonNull(values) && values.length == 0);
  }

  String[] getDefaults() {
    return new String[] {"N1", "N2", "N3"};
  }
}
