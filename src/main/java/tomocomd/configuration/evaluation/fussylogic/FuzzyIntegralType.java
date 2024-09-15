/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tomocomd.configuration.evaluation.fussylogic;

/**
 * @author potter
 */
public enum FuzzyIntegralType {
  CHOQUET("Choquet");

  private final String value;

  FuzzyIntegralType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return this.getValue();
  }

  public static FuzzyIntegralType getEnum(String value) {
    if (value.equals("Choquet")) return CHOQUET;
    throw new IllegalArgumentException();
  }
}
