/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tomocomd.subsetsearch.filters;

import tomocomd.data.PopulationInstances;

/**
 * @author Potter
 */
public class FilterBase extends AbstractMDFilter {
  public FilterBase() {
    super();
    level = 0;
  }

  @Override
  public void filtering(PopulationInstances data) {
    // default function
  }

  @Override
  public void setOptions(String[] options) {
    // default function
  }
}
