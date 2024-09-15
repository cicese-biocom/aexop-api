/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tomocomd.configuration.evaluation.subsetevaluation.subsetfitness;

import lombok.Getter;

/**
 * @author Potter
 */
@Getter
public enum SubsetFitnessType {
  CFS("Merit from Correlation subset");
  private final String value;

  SubsetFitnessType(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return this.getValue();
  }

  public static SubsetFitnessType getEnum(String value) {
    if (value.equals("Merit from Correlation subset")) {
      return CFS;
    }
    throw new IllegalArgumentException();
  }
}
