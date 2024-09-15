/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tomocomd.subsetsearch.evolutive.selection;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import tomocomd.configuration.subsetsearch.operators.GASelectionConfig;
import tomocomd.configuration.subsetsearch.operators.GASelectionType;
import tomocomd.data.PopulationInstances;
import tomocomd.exceptions.AExOpDCSException;

/**
 * @author Potter
 */
@Setter
@Getter
public abstract class AbstractGASelectionOperator {

  protected int cant;
  protected int pro;
  protected GASelectionType type;

  protected AbstractGASelectionOperator(GASelectionConfig conf) {
    cant = conf.getCant();
    type = conf.getType();
  }

  public abstract List<Integer> selection() throws AExOpDCSException;

  public abstract Integer getOneParent() throws AExOpDCSException;

  public abstract void build(PopulationInstances insts) throws AExOpDCSException;
}
