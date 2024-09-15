/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tomocomd.subsetsearch.evolutive.selection;

import java.util.*;
import tomocomd.configuration.subsetsearch.operators.GASelectionConfig;
import tomocomd.data.PopulationInstances;
import tomocomd.exceptions.AExOpDCSException;
import tomocomd.utils.ParseOptions;

/**
 * @author Potter
 */
public class TournamentSelection extends AbstractGASelectionOperator {

  int n;
  int idx;
  Random r;
  HashMap<Integer, Double> valuesList;
  HashMap<Integer, Double> valuesListBack;

  public TournamentSelection(GASelectionConfig conf) {
    super(conf);
    setOptions(conf.getOptions());
    r = new Random();
  }

  @Override
  public void build(PopulationInstances insts) throws AExOpDCSException {
    double[] values = insts.getEva4Desc();
    valuesList = new HashMap<>();
    valuesListBack = new HashMap<>();
    for (int i = 0; i < values.length; i++) {
      valuesList.put(i, values[i]);
      valuesListBack.put(i, values[i]);
    }
    idx = insts.classIndex();
  }

  @Override
  public List<Integer> selection() throws AExOpDCSException {

    List<Integer> part = new LinkedList<>();
    int i = 0;
    while (i < cant) {
      int pos = getOneParent();
      part.add(pos);
      i++;
    }
    return part;
  }

  @Override
  public Integer getOneParent() throws AExOpDCSException {

    if (valuesList.isEmpty() || valuesList.size() < n) {
      valuesList = new HashMap<>(valuesListBack);
    }
    int sel;

    List<Integer> pool = new LinkedList<>();
    while (pool.size() < n) {
      try {
        sel = r.nextInt(valuesList.size());
        int pos = new LinkedList<>(valuesList.keySet()).get(sel);
        if (pos != idx && !pool.contains(pos)) {
          pool.add(pos);
        }
      } catch (Exception e) {
        throw AExOpDCSException.ExceptionType.SELECTION_EXCEPTION.get(
            "Error build the pool for tournament selection", e);
      }
    }

    int pos = pool.get(0);
    double max = valuesList.get(pool.get(0));

    for (Integer p : pool) {
      try {
        if (valuesList.get(p) > max) {
          max = valuesList.get(p);
          pos = p;
        }
      } catch (Exception e) {
        throw AExOpDCSException.ExceptionType.SELECTION_EXCEPTION.get(
            "Error search the next parent in tournament selection", e);
      }
    }

    valuesList.remove(pos);
    return pos;
  }

  public void setOptions(String[] opts) throws AExOpDCSException {
    try {
      Map<String, String> optsValues = ParseOptions.getOption(opts);
      n = Integer.parseInt(optsValues.get("-s"));
    } catch (AExOpDCSException ex) {
      throw AExOpDCSException.ExceptionType.SELECTION_EXCEPTION.get(
          "Error parsing Tournament options", ex);
    } catch (Exception ex) {
      throw AExOpDCSException.ExceptionType.SELECTION_EXCEPTION.get(
          "Error getting Tournament options");
    }
  }
}
