/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tomocomd.subsetsearch.replace.resetpoblation;

import tomocomd.configuration.subsetsearch.operators.GAResetConf;

/**
 * @author Potter
 */
public class ResetPopulation {

  private final int total;
  private final int numIter;

  public ResetPopulation(GAResetConf conf, int numIter) {
    this.numIter = numIter;
    total = conf.getNumIter();
  }

  public boolean resetPopulation() {
    if (total <= 0) return false;
    return numIter % total == 0;
  }
}
