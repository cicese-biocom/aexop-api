/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tomocomd.subsetsearch.evolutive.selection;

import tomocomd.configuration.subsetsearch.operators.GASelectionConfig;
import tomocomd.configuration.subsetsearch.operators.GASelectionType;

/**
 * @author Potter
 */
public class GASelectionFactory {
  private GASelectionFactory() {
    throw new IllegalStateException();
  }

  public static AbstractGASelectionOperator selectionCreator(GASelectionConfig conf) {
    GASelectionType type = conf.getType();
    switch (type) {
      case BEST:
        return new BestSelection(conf);
      case RANDOM:
        return new RandomSelection(conf);
      case ROULETTE:
        return new RouletteSelection(conf);
      case TOURNAMENT:
        return new TournamentSelection(conf);
    }
    throw new IllegalArgumentException("Invalid selection type");
  }
}
