/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tomocomd.subsetsearch.evolutive.selection;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import tomocomd.configuration.subsetsearch.operators.GASelectionConfig;
import tomocomd.data.PopulationInstances;
import tomocomd.exceptions.AExOpDCSException;

/**
 * @author Potter
 */
public class RouletteSelection extends AbstractGASelectionOperator {

  private double[] q;
  int idx;
  Random r;

  public RouletteSelection(GASelectionConfig conf) {
    super(conf);
    r = new Random();
  }

  @Override
  public void build(PopulationInstances insts) throws AExOpDCSException {

    int i;
    double sum = 0.0;
    int numDesc = insts.numAttributes();
    double[] p = new double[numDesc];
    q = new double[numDesc];
    double[] atEva = insts.getEva4Desc();
    double min = Arrays.stream(atEva).min().getAsDouble();

    if (min < 0) {
      for (i = 0; i < numDesc; i++) {
        atEva[i] += Math.abs(min);
      }
    }

    for (i = 0; i < numDesc; i++) {
      sum += atEva[i];
    }

    if (sum != 0) {
      for (i = 0; i < numDesc; i++) {
        p[i] = atEva[i] / sum;
      }
    }
    q[0] = p[0];
    for (i = 1; i < numDesc; i++) {
      q[i] = q[i - 1] + p[i];
    }

    idx = insts.classIndex();
  }

  @Override
  public List<Integer> selection() throws AExOpDCSException {
    int i;
    List<Integer> pos = new LinkedList<>();

    i = 0;
    while (i < cant) {
      pos.add(getOneParent());
      i++;
    }
    return pos;
  }

  @Override
  public Integer getOneParent() throws AExOpDCSException {
    double p1 = r.nextFloat();
    int pi = getPos(p1);
    while (pi == idx) {
      p1 = r.nextFloat();
      pi = getPos(p1);
    }
    return pi;
  }

  private int getPos(double p) {
    int fin;
    int k;
    fin = q.length;

    for (k = 1; k < fin; k++) {
      if (p >= q[k - 1] && p < q[k]) {
        return k - 1;
      }
    }

    return q.length - 1;
  }
}
