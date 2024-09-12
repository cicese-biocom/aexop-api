package tomocomd.data;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;

class PopulationInstancesTest {

  PopulationInstances populationInstances;

  @Test
  void testGetEva4Desc() {
    populationInstances = new PopulationInstances(getData());
    double[] expected = new double[] {1.0, 1.2, 3.4, 5.6, 7.8, 9.0};
    populationInstances.setEva4Desc(expected);
    double[] actual = populationInstances.getEva4Desc();
    for (int i = 0; i < expected.length; i++) {
      assertEquals(expected[i], actual[i]);
    }
  }

  @Test
  void testGetEvaSub() {
    populationInstances = new PopulationInstances(getDataInstances());
    double expected = 3.4;
    populationInstances.setEvaSub(expected);
    double actual = populationInstances.getEvaSub();
    assertEquals(expected, actual);
  }

  private PopulationInstances getData() {
    ArrayList<Attribute> attInfo = new ArrayList<>();
    attInfo.add(new Attribute("act"));
    attInfo.add(new Attribute("att1"));
    attInfo.add(new Attribute("att2"));
    attInfo.add(new Attribute("att3"));
    attInfo.add(new Attribute("att4"));
    attInfo.add(new Attribute("att5"));

    PopulationInstances dataset = new PopulationInstances("dataset", attInfo, 5);
    dataset.add(new DenseInstance(1.0, new double[] {1.0, 1.2, 3.4, 5.6, 7.8, 9.0}));
    dataset.add(new DenseInstance(1.0, new double[] {1.0, 1.2, 1.3, 1.4, 1.6, 8.1}));
    dataset.add(new DenseInstance(1.0, new double[] {1.0, 2.1, 3.2, 4.6, 6.9, 9.8}));
    dataset.add(new DenseInstance(1.0, new double[] {6.0, 8.1, 2.2, 9.6, 2.9, 7.8}));
    dataset.add(new DenseInstance(1.0, new double[] {9.0, 1.8, 8.2, 6.9, 9.2, 8.8}));

    return dataset;
  }

  private Instances getDataInstances() {
    ArrayList<Attribute> attInfo = new ArrayList<>();
    attInfo.add(new Attribute("act"));
    attInfo.add(new Attribute("att1"));
    attInfo.add(new Attribute("att2"));
    attInfo.add(new Attribute("att3"));
    attInfo.add(new Attribute("att4"));
    attInfo.add(new Attribute("att5"));

    Instances dataset = new Instances("dataset", attInfo, 5);
    dataset.add(new DenseInstance(1.0, new double[] {1.0, 1.2, 3.4, 5.6, 7.8, 9.0}));
    dataset.add(new DenseInstance(1.0, new double[] {1.0, 1.2, 1.3, 1.4, 1.6, 8.1}));
    dataset.add(new DenseInstance(1.0, new double[] {1.0, 2.1, 3.2, 4.6, 6.9, 9.8}));
    dataset.add(new DenseInstance(1.0, new double[] {6.0, 8.1, 2.2, 9.6, 2.9, 7.8}));
    dataset.add(new DenseInstance(1.0, new double[] {9.0, 1.8, 8.2, 6.9, 9.2, 8.8}));

    return dataset;
  }
}
