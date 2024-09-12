package tomocomd.configuration.dcs.startpep;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tomocomd.exceptions.AExOpDCSException;

class AggregatorsParamTest {

  AggregatorsParam aggregatorsParam;

  @BeforeEach
  void setUp() {
    aggregatorsParam = new AggregatorsParam();
  }

  @Test
  void init() {
    assertArrayEquals(getDefaultValues(), aggregatorsParam.getValues());
  }

  @Test
  void setDefault() {
    assertArrayEquals(getDefaultValues(), aggregatorsParam.getValues());
  }

  @Test
  void getParams() {
    Map<String, Boolean> params = aggregatorsParam.getParams();
    assertEquals(24, params.size());
    params.values().forEach(Assertions::assertTrue);
  }

  @Test
  void testValidate() {
    AggregatorsParam agg = new AggregatorsParam(aggregatorsParam);
    agg.getStatisticsAggParam().getParams().keySet().forEach(key -> agg.setValue(key, false));
    agg.getGowawaAggParam().getParams().keySet().forEach(key -> agg.setValue(key, false));
    agg.getChoquetAggParam().getParams().keySet().forEach(key -> agg.setValue(key, false));
    agg.getMeansAggParam().getParams().keySet().forEach(key -> agg.setValue(key, false));
    agg.getNormsAggParam().getParams().keySet().forEach(key -> agg.setValue(key, false));
    agg.getInformationAggParam().getParams().keySet().forEach(key -> agg.setValue(key, false));
    agg.validate();
    assertArrayEquals(agg.getStatisticsAggParam().getValues(), agg.getValues());
  }

  @Test
  void setWrongValue() {
    assertThrows(AExOpDCSException.class, () -> aggregatorsParam.setValue("ptt1", false));
  }

  @Test
  void getWrongValue() {
    assertThrows(AExOpDCSException.class, () -> aggregatorsParam.getValue("ptt1"));
  }

  @Test
  void getValue() {
    assertTrue(aggregatorsParam.getValue("TIC"));
  }

  @Test
  void getParamName() {
    assertEquals("Aggregations Param", aggregatorsParam.getParamName());
  }

  @Test
  void testToString() {
    assertEquals(
        "Aggregation Operator={\n"
            + "\t\t Norms=[N1,N2,N3],\n"
            + "\t\t Means=[GM,AM,P2,P3,HM],\n"
            + "\t\t statistics=[V,S,K,SD,VC,RA,Q1,Q2,Q3,I50,MX,MN],\n"
            + "\t\t Choquet=[CHOQUET[A;-0.75;AO2;0.6],CHOQUET[A;-0.75;AO1;0.3],CHOQUET[A;-0.25;AO2;0.6],CHOQUET[A;0.75;AO2;0.6],CHOQUET[A;0.75;AO1;0.2],CHOQUET[A;0.25;AO1;0.9],CHOQUET[A;0.25;AO2;0.6],CHOQUET[A;0.5;AO1;0.2],CHOQUET[A;0.75;AO2;0.5],CHOQUET[A;0.75;AO1;0.8],CHOQUET[A;0.5;AO1;0.9],CHOQUET[A;0.25;AO1;0.8],CHOQUET[A;-0.25;AO2;1.0],CHOQUET[A;-0.25;AO2;0.8],CHOQUET[A;0.5;AO2;0.6],CHOQUET[A;-0.25;AO1;0.8],CHOQUET[A;-0.75;AO1;0.9],CHOQUET[A;0.5;AO2;0.9],CHOQUET[A;0.75;AO1;0.9],CHOQUET[A;-0.75;AO2;0.9],CHOQUET[A;-0.75;AO1;1.0],CHOQUET[A;0.5;AO2;0.8],CHOQUET[A;0.25;AO2;0.5],CHOQUET[A;-0.75;AO2;0.7],CHOQUET[A;-0.75;AO2;1.0],CHOQUET[A;-0.5;AO1;0.3],CHOQUET[A;-0.75;AO2;0.0],CHOQUET[A;-0.75;AO1;0.2],CHOQUET[A;-0.5;AO2;0.0],CHOQUET[A;0.5;AO1;0.8],CHOQUET[A;0.25;AO1;0.2],CHOQUET[A;-0.5;AO1;0.2],CHOQUET[A;0.5;AO2;0.5],CHOQUET[D;-0.75;AO2;0.6],CHOQUET[D;-0.75;AO1;0.3],CHOQUET[D;-0.25;AO2;0.6],CHOQUET[D;0.75;AO2;0.6],CHOQUET[D;0.75;AO1;0.2],CHOQUET[D;0.25;AO1;0.9],CHOQUET[D;0.25;AO2;0.6],CHOQUET[D;0.5;AO1;0.2],CHOQUET[D;0.75;AO2;0.5],CHOQUET[D;0.75;AO1;0.8],CHOQUET[D;0.5;AO1;0.9],CHOQUET[D;0.25;AO1;0.8],CHOQUET[D;-0.25;AO2;1.0],CHOQUET[D;-0.25;AO2;0.8],CHOQUET[D;0.5;AO2;0.6],CHOQUET[D;-0.25;AO1;0.8],CHOQUET[D;-0.75;AO1;0.9],CHOQUET[D;0.5;AO2;0.9],CHOQUET[D;0.75;AO1;0.9],CHOQUET[D;-0.75;AO2;0.9],CHOQUET[D;-0.75;AO1;1.0],CHOQUET[D;0.5;AO2;0.8],CHOQUET[D;0.25;AO2;0.5],CHOQUET[D;-0.75;AO2;0.7],CHOQUET[D;-0.75;AO2;1.0],CHOQUET[D;-0.5;AO1;0.3],CHOQUET[D;-0.75;AO2;0.0],CHOQUET[D;-0.75;AO1;0.2],CHOQUET[D;-0.5;AO2;0.0],CHOQUET[D;0.5;AO1;0.8],CHOQUET[D;0.25;AO1;0.2],CHOQUET[D;-0.5;AO1;0.2],CHOQUET[D;0.5;AO2;0.5]],\n"
            + "\t\t Gowawa=[v[0.9;1;AO2-OWA;1.0;0.0;1;ES2-OWA;0.9;0.0],GOWAWA[1.0;1;AO2-OWA;1.0;0.0;1;NONE;0.0;0.0],GOWAWA[0.1;1;S-OWA;1.0;0.0;1;ES2-OWA;0.9;0.0],GOWAWA[0.2;1;S-OWA;1.0;0.0;1;ES2-OWA;0.9;0.0],GOWAWA[0.7;1;S-OWA;0.8;0.2;1;ES2-OWA;0.9;0.0],GOWAWA[0.8;1;S-OWA;1.0;0.0;1;ES2-OWA;0.9;0.0],GOWAWA[0.5;1;ES1-OWA;0.7;0.0;1;ES2-OWA;0.9;0.0],GOWAWA[0.2;1;AO2-OWA;1.0;0.0;1;ES2-OWA;0.9;0.0],GOWAWA[1.0;1;ES1-OWA;0.7;0.0;1;NONE;0.0;0.0],GOWAWA[0.5;1;S-OWA;1.0;0.0;1;ES2-OWA;0.9;0.0],GOWAWA[0.9;1;ES1-OWA;0.7;0.0;1;ES2-OWA;0.9;0.0],GOWAWA[0.2;1;ES1-OWA;0.7;0.0;1;ES2-OWA;0.9;0.0],GOWAWA[0.7;1;ES1-OWA;0.7;0.0;1;ES2-OWA;0.9;0.0],GOWAWA[0.6;1;AO2-OWA;1.0;0.0;1;ES2-OWA;0.9;0.0],GOWAWA[0.5;1;AO2-OWA;1.0;0.0;1;ES2-OWA;0.9;0.0],GOWAWA[1.0;1;S-OWA;0.8;0.2;1;NONE;0.0;0.0],GOWAWA[0.7;1;AO2-OWA;1.0;0.0;1;ES2-OWA;0.9;0.0],GOWAWA[0.0;1;NONE;0.0;0.0;1;S-OWA;1.0;0.0],GOWAWA[0.1;0;W-OWA;0.1;0.6;2;W-OWA;0.1;0.2],GOWAWA[0.0;1;NONE;0.0;0.0;2;W-OWA;0.4;0.5],GOWAWA[0.0;1;NONE;0.0;0.0;2;W-OWA;0.7;0.8],GOWAWA[0.1;2;ES2-OWA;0.9;0.0;2;W-OWA;0.3;0.4],GOWAWA[0.3;1;S-OWA;1.0;0.0;1;ES2-OWA;0.9;0.0],GOWAWA[0.4;1;ES1-OWA;0.7;0.0;1;ES2-OWA;0.9;0.0],GOWAWA[0.9;1;S-OWA;1.0;0.0;1;ES2-OWA;0.9;0.0],GOWAWA[0.0;1;NONE;0.0;0.0;2;W-OWA;0.5;0.6],GOWAWA[0.1;0;AO1-OWA;1.0;0.0;2;S-OWA;0.8;0.1],GOWAWA[0.0;1;NONE;0.0;0.0;2;W-OWA;0.5;0.7],GOWAWA[0.6;1;S-OWA;1.0;0.0;2;W-OWA;0.9;1.0],GOWAWA[0.1;2;ES2-OWA;0.9;0.0;2;W-OWA;0.2;0.3],GOWAWA[1.0;1;ES1-OWA;0.7;0.0;1;NONE;0.0;0.0],GOWAWA[1.0;1;S-OWA;1.0;0.0;1;NONE;0.0;0.0],GOWAWA[1.0;2;ES2-OWA;0.9;0.0;1;NONE;0.0;0.0],GOWAWA[0.5;1;AO2-OWA;1.0;0.0;1;ES2-OWA;0.9;0.0],GOWAWA[1.0;1;S-OWA;0.8;0.2;1;NONE;0.0;0.0],GOWAWA[0.0;1;NONE;0.0;0.0;0;S-OWA;0.0;1.0],GOWAWA[0.7;2;ES2-OWA;0.9;0.0;0;S-OWA;0.0;1.0],GOWAWA[0.1;2;ES2-OWA;0.9;0.0;2;W-OWA;0.7;0.8],GOWAWA[0.1;2;ES2-OWA;0.9;0.0;0;W-OWA;0.0;0.1],GOWAWA[0.9;1;AO2-OWA;1.0;0.0;1;ES2-OWA;0.9;0.0],GOWAWA[0.2;2;ES2-OWA;0.9;0.0;2;W-OWA;0.4;0.6],GOWAWA[0.3;2;S-OWA;0.6;0.0;2;W-OWA;0.9;1.0],GOWAWA[0.4;1;AO2-OWA;1.0;0.0;1;ES2-OWA;0.9;0.0],GOWAWA[1.0;1;AO2-OWA;1.0;0.0;1;NONE;0.0;0.0],GOWAWA[0.0;1;NONE;0.0;0.0;1;S-OWA;1.0;0.0],GOWAWA[0.8;1;AO2-OWA;1.0;0.0;1;ES2-OWA;0.9;0.0],GOWAWA[0.9;1;ES1-OWA;0.7;0.0;1;ES2-OWA;0.9;0.0],GOWAWA[0.2;2;S-OWA;0.6;0.0;2;S-OWA;0.8;0.1]],\n"
            + "\t\t Information=[TIC,SIC]\n"
            + "\t\t}",
        aggregatorsParam.toString());
  }

  @Test
  void testEquals() {}

  @Test
  void testHashCode() {}

  @Test
  void getNormsAggParam() {}

  @Test
  void getMeansAggParam() {}

  @Test
  void getStatisticsAggParam() {}

  @Test
  void getChoquetAggParam() {}

  @Test
  void getGowawaAggParam() {}

  @Test
  void getInformationAggParam() {}

  String[] getDefaultValues() {
    return new String[] {
      "N1",
      "N2",
      "N3",
      "GM",
      "AM",
      "P2",
      "P3",
      "HM",
      "V",
      "S",
      "K",
      "SD",
      "VC",
      "RA",
      "Q1",
      "Q2",
      "Q3",
      "I50",
      "MX",
      "MN",
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
      "CHOQUET[D;0.5;AO2;0.5]",
      "v[0.9;1;AO2-OWA;1.0;0.0;1;ES2-OWA;0.9;0.0]",
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
      "GOWAWA[0.2;2;S-OWA;0.6;0.0;2;S-OWA;0.8;0.1]",
      "TIC",
      "SIC"
    };
  }
}
