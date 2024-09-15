/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tomocomd.subsetsearch.replace.replacesubpopulation;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import tomocomd.configuration.dcs.PDType;
import tomocomd.data.PopulationInstances;
import tomocomd.exceptions.AExOpDCSException;
import tomocomd.utils.SortInstancesByAttribute;

/**
 * @author Potter
 */
public class ReplacingWorsts implements IMDGAReplace {

  public ReplacingWorsts() {
    // empty constructor
  }

  @Override
  public PopulationInstances makeReplace(
      PDType type,
      PopulationInstances m,
      List<Integer> parents,
      PopulationInstances childrenInstances)
      throws AExOpDCSException {
    double[] values = m.getEva4Desc();

    Set<Integer> pos;
    List<Integer> vOrdered = SortInstancesByAttribute.sortAndGetPos(values, true);

    int numAttChildren = childrenInstances.numAttributes();
    pos = new LinkedHashSet<>(vOrdered.subList(vOrdered.size() - numAttChildren, vOrdered.size()));
    if (pos.contains(m.classIndex())) {
      pos.remove(m.classIndex());
      pos.add(vOrdered.get(vOrdered.size() - numAttChildren - 1));
    }

    try {
      m.deleteAttributesByPos(pos, false);
    } catch (Exception ex) {
      throw AExOpDCSException.ExceptionType.REPLACE_EXCEPTION.get(
          "Error removing the worst attributes", ex);
    }

    try {
      return PopulationInstances.merge(m, childrenInstances);
    } catch (Exception ex) {
      throw AExOpDCSException.ExceptionType.REPLACE_EXCEPTION.get(
          "Problems merging instances at replacing worsts step", ex);
    }
  }
}
