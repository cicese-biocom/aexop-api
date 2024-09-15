/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tomocomd.subsetsearch.replace.replacesubpopulation;

import tomocomd.configuration.subsetsearch.operators.GAMDReplaceConfig;
import tomocomd.configuration.subsetsearch.operators.GAMDReplaceType;

/**
 * @author Potter
 */
public class MDGAReplaceFactory {

  protected MDGAReplaceFactory() {
    throw new IllegalStateException();
  }

  public static IMDGAReplace getRecombination(GAMDReplaceConfig conf) {
    if (conf.getType() == GAMDReplaceType.PARENT) return new ReplacingParents();
    else if (conf.getType() == GAMDReplaceType.WORST) return new ReplacingWorsts();
    throw new IllegalStateException("Invalid recombination type");
  }
}
