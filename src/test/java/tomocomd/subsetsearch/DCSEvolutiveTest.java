package tomocomd.subsetsearch;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tomocomd.configuration.dcs.AAttributeDCS;
import tomocomd.configuration.dcs.AHeadEntity;
import tomocomd.configuration.dcs.startpep.StartpepDCS;
import tomocomd.configuration.subsetsearch.DCSEvolutiveConfig;
import tomocomd.data.PopulationInstances;
import tomocomd.io.CSVManage;
import weka.core.Attribute;
import weka.core.DenseInstance;

class DCSEvolutiveTest {
  DCSEvolutive dcsEvolutive;

  @BeforeEach
  void setUp() {
    DCSEvolutiveConfig dcsEvolutiveConfig = new DCSEvolutiveConfig();
    dcsEvolutiveConfig.getSelConf().setCant(2);
    AAttributeDCS aMdDCS = new StartpepDCS();
    dcsEvolutive = new DCSEvolutive(dcsEvolutiveConfig, aMdDCS, null);
  }

  @Test
  void constructor() {
    assertEquals(100, dcsEvolutive.getNumDesc());
    assertNotNull(dcsEvolutive);
  }

  @Test
  void resetBestSubsetNull() {
    dcsEvolutive.resetBestSubset(null);
    assertNull(dcsEvolutive.getBestSubset());
  }

  @Test
  void resetBestSubset() {
    PopulationInstances populationInstances =
        new PopulationInstances(CSVManage.loadCSV("csv/classData.csv"));
    dcsEvolutive.resetBestSubset(populationInstances);
    PopulationInstances data = dcsEvolutive.getBestSubset();
    assertEquals(populationInstances.numAttributes(), data.numAttributes());
    assertEquals(populationInstances.size(), data.size());
  }

  @Test
  void populationSizeNull() {
    assertEquals(0, dcsEvolutive.populationSize());
  }

  @Test
  void populationSize() {
    PopulationInstances populationInstances =
        new PopulationInstances(CSVManage.loadCSV("csv/classData.csv"));
    dcsEvolutive.resetBestSubset(populationInstances);
    assertEquals(populationInstances.numAttributes(), dcsEvolutive.populationSize());
  }

  @Test
  void generateHeads() {
    Set<AHeadEntity> heads = dcsEvolutive.generateHeadings(100);
    assertEquals(100, heads.size());
  }

  @Test
  void mergeSubsetTest() {
    PopulationInstances populationInstances =
        new PopulationInstances(CSVManage.loadCSV("csv/classData.csv"));
    populationInstances.setClassIndex(0);
    dcsEvolutive.resetBestSubset(populationInstances);

    ArrayList<Attribute> attributes = new ArrayList<>();
    attributes.add(new Attribute("newAtt"));
    PopulationInstances newData = new PopulationInstances("newData", attributes, 1);

    double[] values = new double[populationInstances.numInstances()];
    for (int i = 0; i < 54; i++) {
      newData.add(new DenseInstance(1, new double[] {i}));
      values[i] = i;
    }

    dcsEvolutive.mergeSubset(newData);
    PopulationInstances pInew = dcsEvolutive.getBestSubset();
    assertEquals(26, pInew.numAttributes());
    assertArrayEquals(values, pInew.attributeToDoubleArray(pInew.numAttributes() - 1));
  }

  @Test
  void applyFilterTest() {
    PopulationInstances populationInstances =
        new PopulationInstances(CSVManage.loadCSV("csv/classData.csv"));
    populationInstances.setClassIndex(0);
    dcsEvolutive.resetBestSubset(populationInstances);

    ArrayList<Attribute> attributes = new ArrayList<>();
    attributes.add(new Attribute("newAtt"));
    PopulationInstances newData = new PopulationInstances("newData", attributes, 1);

    for (int i = 0; i < 54; i++) {
      newData.add(new DenseInstance(1, new double[] {Double.NaN}));
    }

    dcsEvolutive.mergeSubset(newData);

    dcsEvolutive.applyFilter();
    PopulationInstances pInew = dcsEvolutive.getBestSubset();
    assertEquals(22, pInew.numAttributes());
    assertArrayEquals(getLastDesc(), pInew.attributeToDoubleArray(pInew.numAttributes() - 1));
  }

  @Test
  void selectionK4CooperativeBestSubsetNull() {
    PopulationInstances data = dcsEvolutive.selectionK4Cooperative();
    assertEquals(0, data.numAttributes());
    assertEquals("Empty", data.relationName());
  }

  @Test
  void selectionK4Cooperative() {
    PopulationInstances targetInstances =
        new PopulationInstances(CSVManage.loadCSV("csv/classData.csv"));
    targetInstances.setClassIndex(0);
    dcsEvolutive.resetBestSubset(targetInstances);

    PopulationInstances data = dcsEvolutive.selectionK4Cooperative();
    assertEquals(2, data.numAttributes());
  }

  @Test
  void evaluateMdTest() {
    PopulationInstances targetInstances =
        new PopulationInstances(CSVManage.loadCSV("csv/classData.csv"));
    targetInstances.setClassIndex(0);
    dcsEvolutive.resetBestSubset(targetInstances);

    ArrayList<Attribute> attributes = new ArrayList<>();
    attributes.add(new Attribute("newAtt"));
    PopulationInstances newData = new PopulationInstances("newData", attributes, 1);

    for (int i = 0; i < 54; i++) {
      newData.add(new DenseInstance(1, new double[] {Double.NaN}));
    }

    dcsEvolutive.evaluateMd(newData);

    assertArrayEquals(getEva(), dcsEvolutive.getBestSubset().getEva4Desc());
  }

  @Test
  void geneticOperators() throws NoSuchFieldException, IllegalAccessException {
    PopulationInstances populationInstances =
        new PopulationInstances(CSVManage.loadCSV("csv/classData.csv"));
    populationInstances.setClassIndex(0);
    dcsEvolutive.resetBestSubset(populationInstances);
    dcsEvolutive.evaluateMd(null);
    dcsEvolutive.geneticsOperators(4);

    Field parentsField = DCSEvolutive.class.getDeclaredField("children");
    parentsField.setAccessible(true);
    Set<AHeadEntity> heads = (Set<AHeadEntity>) parentsField.get(dcsEvolutive);
    assertEquals(4, heads.size());
  }

  double[] getLastDesc() {
    return new double[] {
      0.0, 0.0, 0.0, 0.0, 9.66E-7, 2.7E-6, 2.61E-7, 0.0, 0.0, 0.0, 4.55E-8, 5.28E-7, 0.0, 0.0, 0.0,
      0.0, 3.14E-7, 0.0, 3.82E-8, 0.0, 4.66E-8, 0.0, 0.0, 0.0, 0.0, 0.0, -1.32E-10, 0.0, -2.06E-10,
      0.0, 3.81E-11, -8.49E-9, -4.39E-9, 1.18E-7, 0.0, 0.0, 0.0, 0.0, -6.15E-7, 0.0, 0.0, 0.0, 0.0,
      0.0, 0.0, 0.0, 6.79E-10, 2.63E-8, 2.67E-6, 7.56E-8, -4.29E-7, -1.98E-7, 6.89E-10, 0.0
    };
  }

  double[] getEva() {
    return new double[] {
      0.0,
      0.2889280473166174,
      0.3051300579567227,
      0.29625134499572336,
      0.29780011649542876,
      0.2675583531641339,
      0.26012306979664246,
      0.2983503949143129,
      0.30025388303871936,
      0.2771619717464975,
      0.34717667873957264,
      0.2774199412490376,
      0.2608182953736458,
      0.34162327509349516,
      0.26581832937735095,
      0.2544950803422796,
      0.3118819233659214,
      0.2703481212937009,
      0.2926781056323866,
      0.2599944968597868,
      0.27919879806899744,
      0.30975201984055223,
      0.2881253711893365,
      0.2607396796874822,
      0.27694343688778833
    };
  }
}
