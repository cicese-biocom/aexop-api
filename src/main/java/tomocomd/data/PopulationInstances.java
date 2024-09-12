package tomocomd.data;

import java.util.*;
import weka.core.Attribute;
import weka.core.Instances;

public class PopulationInstances extends Instances {
  private double[] eva4Desc;
  private double evaSub;

  public PopulationInstances(Instances dataset) {
    super(dataset);
    eva4Desc = new double[dataset.numAttributes()];
    evaSub = 0.0;
  }

  public PopulationInstances(PopulationInstances populationInstances) {
    super(populationInstances);
    this.eva4Desc = new double[populationInstances.numAttributes()];
    System.arraycopy(
        populationInstances.getEva4Desc(),
        0,
        this.eva4Desc,
        0,
        populationInstances.numAttributes());
    this.evaSub = populationInstances.getEvaSub();
  }

  public PopulationInstances(String name, ArrayList<Attribute> attInfo, int capacity) {
    super(name, attInfo, capacity);
    eva4Desc = new double[attInfo.size()];
    evaSub = 0.0;
  }

  public double[] getEva4Desc() {
    return eva4Desc;
  }

  public double getEvaSub() {
    return evaSub;
  }

  public void setEva4Desc(double[] eva4Desc) {
    this.eva4Desc = eva4Desc;
  }

  public void setEvaSub(double evaSub) {
    this.evaSub = evaSub;
  }
}
