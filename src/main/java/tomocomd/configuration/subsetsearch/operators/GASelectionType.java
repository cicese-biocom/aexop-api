/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tomocomd.configuration.subsetsearch.operators;

import lombok.Getter;

/**
 * @author Potter
 */
@Getter
public enum GASelectionType {
  BEST("Best"),
  RANDOM("Random"),
  ROULETTE("Roulette wheel"),
  TOURNAMENT("Tournament");
  private final String value;

  GASelectionType(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return this.getValue();
  }

  public static GASelectionType getEnum(String value) {
    switch (value) {
      case "Best":
        return BEST;
      case "Random":
        return RANDOM;
      case "Roulette wheel":
        return ROULETTE;
      case "Tournament":
        return TOURNAMENT;
      default:
        throw new IllegalArgumentException();
    }
  }
}
