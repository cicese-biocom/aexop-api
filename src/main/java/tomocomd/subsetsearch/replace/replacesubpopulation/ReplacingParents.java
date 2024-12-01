/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tomocomd.subsetsearch.replace.replacesubpopulation;

import java.util.LinkedHashSet;
import java.util.List;
import tomocomd.configuration.dcs.AttributeType;
import tomocomd.data.PopulationInstances;
import tomocomd.exceptions.AExOpDCSException;

/**
 * @author Potter
 */
public class ReplacingParents implements IMDGAReplace {

  @Override
  public PopulationInstances makeReplace(
      AttributeType type,
      PopulationInstances m,
      List<Integer> parents,
      PopulationInstances childrenInstances)
      throws AExOpDCSException {

    try {
      m.deleteAttributesByPos(new LinkedHashSet<>(parents), false);
    } catch (Exception ex) {
      throw AExOpDCSException.ExceptionType.REPLACE_EXCEPTION.get(
          "Error remove parent attributes", ex);
    }

    try {
      return PopulationInstances.merge(m, childrenInstances);
    } catch (Exception ex) {
      throw AExOpDCSException.ExceptionType.REPLACE_EXCEPTION.get(
          "Problems computing and merging new attributes to data sets", ex);
    }
  }
}
