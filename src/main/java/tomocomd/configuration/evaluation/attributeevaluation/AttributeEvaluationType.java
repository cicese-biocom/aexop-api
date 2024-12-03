/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tomocomd.configuration.evaluation.attributeevaluation;

import lombok.Getter;

/**
 * @author Potter
 */
@Getter
public enum AttributeEvaluationType {
  SE("Shannon entropy"),
  IMPURITY("Impurity"),
  RELIEFF("ReliefF"),
  R2("R2"),
  CHOQUET("Choquet integral");

  private final String value;

  AttributeEvaluationType(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return this.getValue();
  }
}
