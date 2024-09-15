/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tomocomd.subsetsearch.evolutive.selection;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import lombok.Getter;
import tomocomd.configuration.subsetsearch.operators.GASelectionConfig;
import tomocomd.data.PopulationInstances;
import tomocomd.exceptions.AExOpDCSException;

/**
 * @author Potter
 */
@Getter
public class RandomSelection extends AbstractGASelectionOperator {

  private int numAtt;
  private int idx;
  private final Random r;

  public RandomSelection(GASelectionConfig conf) {
    super(conf);
    r = new Random();
  }

  @Override
  public void build(PopulationInstances insts) throws AExOpDCSException {
    numAtt = insts.numAttributes();
    idx = insts.classIndex();
  }

  @Override
  public List<Integer> selection() throws AExOpDCSException {
    int i;

    List<Integer> result = new LinkedList<>();

    i = 0;
    while (i < cant) {
      int p = r.nextInt(numAtt);
      if (!(result.contains(p)) && p != idx) {
        result.add(p);
        i++;
      }
    }
    return result;
  }

  @Override
  public Integer getOneParent() throws AExOpDCSException {
    int p = r.nextInt(numAtt);
    while (p == idx) {
      p = r.nextInt(numAtt);
    }
    return p;
  }
}
