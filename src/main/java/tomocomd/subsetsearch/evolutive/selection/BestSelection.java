/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tomocomd.subsetsearch.evolutive.selection;

import java.util.LinkedList;
import java.util.List;
import tomocomd.configuration.subsetsearch.operators.GASelectionConfig;
import tomocomd.data.PopulationInstances;
import tomocomd.exceptions.AExOpDCSException;
import tomocomd.utils.SortInstancesByAttribute;

/**
 * @author Potter
 */
public class BestSelection extends AbstractGASelectionOperator {

  List<Integer> vOrdered;
  int numSelected;
  int idx;

  public BestSelection(GASelectionConfig conf) {
    super(conf);
  }

  @Override
  public void build(PopulationInstances insts) throws AExOpDCSException {
    vOrdered = SortInstancesByAttribute.sortAndGetPos(insts.getEva4Desc(), true);
    numSelected = -1;
    idx = insts.classIndex();
  }

  @Override
  public List<Integer> selection() throws AExOpDCSException {

    try {
      List<Integer> pos = new LinkedList<>();
      for (int i = 0; i < cant; i++) pos.add(getOneParent());
      return pos;
    } catch (Exception ex) {
      throw AExOpDCSException.ExceptionType.SELECTION_EXCEPTION.get(
          "Problems selecting new parents", ex);
    }
  }

  @Override
  public Integer getOneParent() throws AExOpDCSException {
    numSelected++;
    if (numSelected == vOrdered.size()) numSelected = 0;
    int pos = vOrdered.get(numSelected);
    if (pos == idx) {
      numSelected++;
      if (numSelected == vOrdered.size()) numSelected = 0;
      pos = vOrdered.get(numSelected);
    }
    return pos;
  }
}
