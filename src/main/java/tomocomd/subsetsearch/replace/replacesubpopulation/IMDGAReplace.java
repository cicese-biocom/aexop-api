/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tomocomd.subsetsearch.replace.replacesubpopulation;

import java.util.List;
import tomocomd.configuration.dcs.AttributeType;
import tomocomd.data.PopulationInstances;
import tomocomd.exceptions.AExOpDCSException;

/**
 * @author Potter
 */
public interface IMDGAReplace {
  PopulationInstances makeReplace(
      AttributeType type,
      PopulationInstances m,
      List<Integer> parents,
      PopulationInstances childrenInstances)
      throws AExOpDCSException;
}
