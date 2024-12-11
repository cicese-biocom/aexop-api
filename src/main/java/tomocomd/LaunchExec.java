/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tomocomd;

import tomocomd.configuration.dcs.DCSFactory;
import tomocomd.configuration.dcs.HeadFactory;
import tomocomd.configuration.subsetsearch.AexopConfig;
import tomocomd.data.PopulationInstances;
import tomocomd.descriptors.AttributeComputerFactory;
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
      AexopConfig conf,
      String outFile,
      String inputObjectFile,
      PopulationInstances target,
      AttributeComputerFactory attributeComputerFactory,
      HeadFactory headFactory,
      DCSFactory dcsFactory)
      throws AExOpDCSException {

    AexopDcs algorithm =
        new AexopDcs(
            conf,
            outFile,
            inputObjectFile,
            target,
            attributeComputerFactory,
            headFactory,
            dcsFactory);
    algorithm.compute();
  }
}
