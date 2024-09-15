/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tomocomd.configuration.evaluation.fussylogic;

/**
 * @author potter
 */
public enum FuzzyMeasureType {
  LAMBDA("Sugeno lambda measure"),
  Q("Q-measure");

  private final String value;

  FuzzyMeasureType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return this.getValue();
  }

  public static FuzzyMeasureType getEnum(String value) {
    switch (value) {
      case "Sugeno lambda measure":
        return LAMBDA;
      case "Q-measure":
        return Q;
      default:
        throw new IllegalArgumentException();
    }
  }
}
