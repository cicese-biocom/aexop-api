/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tomocomd;

import tomocomd.configuration.subsetsearch.AexopConfig;
import tomocomd.exceptions.AExOpDCSException;
import tomocomd.subsetsearch.AexopDcs;

/**
 * @author potter
 */
public class LaunchExec {

  private LaunchExec() {
    throw new IllegalStateException("Error starting LaunchExec class");
  }

  public static void launchExec(
      AexopConfig conf, String outFile, String fastaFile, String pathCsvTarget)
      throws AExOpDCSException {

    AexopDcs algorithm = new AexopDcs(conf, outFile, fastaFile, pathCsvTarget);
    algorithm.compute();
  }
}
