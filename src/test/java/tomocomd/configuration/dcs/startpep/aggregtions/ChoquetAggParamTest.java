package tomocomd.configuration.dcs.startpep.aggregtions;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ChoquetAggParamTest {
  ChoquetAggParam choquetAggParam;

  @BeforeEach
  void setUp() {
    choquetAggParam = new ChoquetAggParam();
  }

  @Test
  void setDefault() {
    assertTrue(choquetAggParam.getValue("CHOQUET"));
  }

  @Test
  void getValues() {
    assertArrayEquals(defaultChoquet, choquetAggParam.getValues());
  }

  @Test
  void getParamName() {
    assertEquals("Choquet aggregators", choquetAggParam.getParamName());
  }

  @Test
  void validate() {
    ChoquetAggParam choquet = new ChoquetAggParam(choquetAggParam);
    choquet.setValue("CHOQUET", false);
    String[] values = choquet.getValues();
    assertTrue(Objects.nonNull(values) && values.length == 0);
  }

  private static final String[] defaultChoquet = {
    "CHOQUET[A;-0.75;AO2;0.6]",
    "CHOQUET[A;-0.75;AO1;0.3]",
    "CHOQUET[A;-0.25;AO2;0.6]",
    "CHOQUET[A;0.75;AO2;0.6]",
    "CHOQUET[A;0.75;AO1;0.2]",
    "CHOQUET[A;0.25;AO1;0.9]",
    "CHOQUET[A;0.25;AO2;0.6]",
    "CHOQUET[A;0.5;AO1;0.2]",
    "CHOQUET[A;0.75;AO2;0.5]",
    "CHOQUET[A;0.75;AO1;0.8]",
    "CHOQUET[A;0.5;AO1;0.9]",
    "CHOQUET[A;0.25;AO1;0.8]",
    "CHOQUET[A;-0.25;AO2;1.0]",
    "CHOQUET[A;-0.25;AO2;0.8]",
    "CHOQUET[A;0.5;AO2;0.6]",
    "CHOQUET[A;-0.25;AO1;0.8]",
    "CHOQUET[A;-0.75;AO1;0.9]",
    "CHOQUET[A;0.5;AO2;0.9]",
    "CHOQUET[A;0.75;AO1;0.9]",
    "CHOQUET[A;-0.75;AO2;0.9]",
    "CHOQUET[A;-0.75;AO1;1.0]",
    "CHOQUET[A;0.5;AO2;0.8]",
    "CHOQUET[A;0.25;AO2;0.5]",
    "CHOQUET[A;-0.75;AO2;0.7]",
    "CHOQUET[A;-0.75;AO2;1.0]",
    "CHOQUET[A;-0.5;AO1;0.3]",
    "CHOQUET[A;-0.75;AO2;0.0]",
    "CHOQUET[A;-0.75;AO1;0.2]",
    "CHOQUET[A;-0.5;AO2;0.0]",
    "CHOQUET[A;0.5;AO1;0.8]",
    "CHOQUET[A;0.25;AO1;0.2]",
    "CHOQUET[A;-0.5;AO1;0.2]",
    "CHOQUET[A;0.5;AO2;0.5]",
    "CHOQUET[D;-0.75;AO2;0.6]",
    "CHOQUET[D;-0.75;AO1;0.3]",
    "CHOQUET[D;-0.25;AO2;0.6]",
    "CHOQUET[D;0.75;AO2;0.6]",
    "CHOQUET[D;0.75;AO1;0.2]",
    "CHOQUET[D;0.25;AO1;0.9]",
    "CHOQUET[D;0.25;AO2;0.6]",
    "CHOQUET[D;0.5;AO1;0.2]",
    "CHOQUET[D;0.75;AO2;0.5]",
    "CHOQUET[D;0.75;AO1;0.8]",
    "CHOQUET[D;0.5;AO1;0.9]",
    "CHOQUET[D;0.25;AO1;0.8]",
    "CHOQUET[D;-0.25;AO2;1.0]",
    "CHOQUET[D;-0.25;AO2;0.8]",
    "CHOQUET[D;0.5;AO2;0.6]",
    "CHOQUET[D;-0.25;AO1;0.8]",
    "CHOQUET[D;-0.75;AO1;0.9]",
    "CHOQUET[D;0.5;AO2;0.9]",
    "CHOQUET[D;0.75;AO1;0.9]",
    "CHOQUET[D;-0.75;AO2;0.9]",
    "CHOQUET[D;-0.75;AO1;1.0]",
    "CHOQUET[D;0.5;AO2;0.8]",
    "CHOQUET[D;0.25;AO2;0.5]",
    "CHOQUET[D;-0.75;AO2;0.7]",
    "CHOQUET[D;-0.75;AO2;1.0]",
    "CHOQUET[D;-0.5;AO1;0.3]",
    "CHOQUET[D;-0.75;AO2;0.0]",
    "CHOQUET[D;-0.75;AO1;0.2]",
    "CHOQUET[D;-0.5;AO2;0.0]",
    "CHOQUET[D;0.5;AO1;0.8]",
    "CHOQUET[D;0.25;AO1;0.2]",
    "CHOQUET[D;-0.5;AO1;0.2]",
    "CHOQUET[D;0.5;AO2;0.5]"
  };
}
