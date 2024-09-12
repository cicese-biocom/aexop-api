package tomocomd.configuration.dcs.startpep.aggregtions;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StatisticsAggParamTest {

  StatisticsAggParam statisticsAggParam;

  @BeforeEach
  void setUp() {
    statisticsAggParam = new StatisticsAggParam();
  }

  @Test
  void setDefault() {
    assertArrayEquals(getDefaults(), statisticsAggParam.getValues());
  }

  @Test
  void getParamName() {
    assertEquals("Statistics aggregators", statisticsAggParam.getParamName());
  }

  @Test
  void testValidate() {
    StatisticsAggParam stats = new StatisticsAggParam(statisticsAggParam);
    stats.getParams().keySet().forEach(key -> stats.setValue(key, false));
    String[] values = stats.getValues();
    assertTrue(Objects.nonNull(values) && values.length == 0);
  }

  String[] getDefaults() {
    return new String[] {"V", "S", "K", "SD", "VC", "RA", "Q1", "Q2", "Q3", "I50", "MX", "MN"};
  }
}
