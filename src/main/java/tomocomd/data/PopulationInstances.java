package tomocomd.data;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.Getter;
import lombok.Setter;
import tomocomd.exceptions.AExOpDCSException;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

@Getter
@Setter
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

  public void setEva4DescPos(int j, double eva) {
    this.eva4Desc[j] = eva;
  }

  public double getEva4Desc(int j) {
    return this.eva4Desc[j];
  }

  public void deleteAttributesByPos(Set<Integer> pos, boolean reverse) throws AExOpDCSException {
    Integer[] posInt;
    if (reverse) {
      int numDesc = numAttributes();
      List<Integer> posList = new LinkedList<>();
      for (int i = 0; i < numDesc; i++) {
        if (!pos.contains(i)) {
          posList.add(i);
        }
      }
      posInt = posList.stream().sorted(Comparator.reverseOrder()).toArray(Integer[]::new);
    } else {
      posInt = pos.stream().sorted(Comparator.reverseOrder()).toArray(Integer[]::new);
    }

    for (Integer i : posInt) {
      deleteAttributeAt(i);
    }

    if (posInt.length > 0) {
      double[] copy = new double[numAttributes()];

      int posNew = 0;
      for (int i = 0; i < eva4Desc.length; i++) {
        int posI = i;
        Optional<Integer> found = Arrays.stream(posInt).filter(j -> j == posI).findFirst();
        if (!found.isPresent()) copy[posNew++] = eva4Desc[i];
      }
      eva4Desc = new double[copy.length];
      System.arraycopy(copy, 0, eva4Desc, 0, eva4Desc.length);
    }
  }

  public static PopulationInstances merge(PopulationInstances insts11, PopulationInstances insts12)
      throws IllegalArgumentException {
    int i;
    int pos;

    if (insts11 == null && insts12 == null) {
      return null;
    } else if (insts11 != null && insts12 == null) {
      return insts11;
    } else if (insts11 == null) {
      return insts12;
    }

    if (insts11.numInstances() != insts12.numInstances()) {
      throw new IllegalArgumentException(
          String.format(
              "Error merge Tomocomd instances, number of instances dont match %d != %d",
              insts11.numInstances(), insts12.numInstances()));
    }

    List<Integer> indexs = new LinkedList<>();

    for (i = 0; i < insts12.numAttributes(); i++) {
      Attribute attThis = insts11.attribute(insts12.attribute(i).name());
      if (attThis == null) {
        indexs.add(i);
      }
    }

    ArrayList<Attribute> names = new ArrayList<>();

    // names from insts11
    for (i = 0; i < insts11.numAttributes(); i++) {
      names.add(insts11.attribute(i));
    }

    // names from insts12
    for (int j : indexs) {
      names.add(insts12.attribute(j));
    }

    PopulationInstances tomIns = new PopulationInstances("Union", names, insts11.numInstances());
    for (i = 0; i < insts11.numInstances(); i++) {
      Instance ins = mergeInstance(insts11.instance(i), insts12.instance(i), indexs);
      tomIns.add(ins);
    }
    double[] newValues = new double[names.size()];
    System.arraycopy(insts11.getEva4Desc(), 0, newValues, 0, insts11.numAttributes());
    pos = insts11.numAttributes();
    for (int j : indexs) newValues[pos++] = insts12.getEva4Desc()[j];

    tomIns.setEva4Desc(newValues);

    // set clas index
    return setClassIndex(insts11, insts12, tomIns);
  }

  private static PopulationInstances setClassIndex(
      PopulationInstances insts11, PopulationInstances insts12, PopulationInstances tomIns) {
    // set clas index
    if (insts11.classIndex() > -1) {
      tomIns.setClassIndex(insts11.classIndex());
    } else {
      if (insts12.classIndex() > -1) {
        String nameIdx2 = insts12.classAttribute().name();
        Attribute attThis = tomIns.attribute(nameIdx2);
        tomIns.setClass(attThis);
      }
    }
    return tomIns;
  }

  private static Instance mergeInstance(Instance inst1, Instance inst2, List<Integer> posToAdd) {
    int m = 0;
    double[] newVals = new double[inst1.numAttributes() + posToAdd.size()];

    int j;
    for (j = 0; j < inst1.numAttributes(); ++m) {
      newVals[m] = inst1.value(j);
      ++j;
    }

    AtomicInteger pos = new AtomicInteger(inst1.numAttributes());
    posToAdd.forEach(pos2 -> newVals[pos.getAndIncrement()] = inst2.value(pos2));

    return new DenseInstance(1.0D, newVals);
  }
}
