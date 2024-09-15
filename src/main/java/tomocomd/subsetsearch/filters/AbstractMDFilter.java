/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tomocomd.subsetsearch.filters;

import lombok.Getter;
import tomocomd.data.PopulationInstances;
import tomocomd.exceptions.AExOpDCSException;

/**
 * @author Potter
 */
@Getter
public abstract class AbstractMDFilter {

  protected int level;

  protected AbstractMDFilter() {}

  public abstract void filtering(PopulationInstances data) throws AExOpDCSException;

  public abstract void setOptions(String[] options) throws AExOpDCSException;

  @Override
  public String toString() {
    return this.getClass().getName();
  }
}
