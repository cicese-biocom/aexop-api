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
public enum GAReplacePoblationType {
  STEADYSTATE("Steady state"),
  DELETEALL("Delete-All"),
  STEADYSTATERESET("Steady state reset");

  private final String value;

  GAReplacePoblationType(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return this.getValue();
  }

  public static GAReplacePoblationType getEnum(String value) {
    switch (value) {
      case "Steady state":
        return STEADYSTATE;
      case "Delete-All":
        return DELETEALL;
      case "Steady state reset":
        return STEADYSTATERESET;
      default:
        throw new IllegalArgumentException();
    }
  }
}
