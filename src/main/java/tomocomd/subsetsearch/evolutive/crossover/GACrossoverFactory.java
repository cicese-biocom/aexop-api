/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tomocomd.subsetsearch.evolutive.crossover;

import tomocomd.configuration.subsetsearch.operators.GACrossoverConf;
import tomocomd.configuration.subsetsearch.operators.GACrossoverType;

/**
 * @author Potter
 */
public class GACrossoverFactory {

  private GACrossoverFactory() {}

  public static AGACrossoverOperation getCrossover(GACrossoverConf conf) {
    if (conf.getType() == GACrossoverType.UNIFORM) return new UniformCrossover(conf);
    else if (conf.getType() == GACrossoverType.HUX) return new HuxCrossover(conf);
    throw new IllegalArgumentException("Crossover type not supported: " + conf.getType());
  }
}
