/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tomocomd.configuration.evaluation.attributeevaluation;

import java.io.Serializable;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import tomocomd.configuration.evaluation.fussylogic.FuzzyIntegralType;
import tomocomd.configuration.evaluation.fussylogic.FuzzyMeasureType;

/**
 * @author Potter
 */
@Data
@Builder
@AllArgsConstructor
public class AttributeEvaluationConf implements Serializable {
  AttributeEvaluationType type;
  String[] option;

  public AttributeEvaluationConf() {
    getDefaultsValues();
  }

  private void getDefaultsValues() {
    type = AttributeEvaluationType.CHOQUET;
    option =
        new String[] {
          "-m",
          FuzzyMeasureType.Q.toString(),
          "-i",
          FuzzyIntegralType.CHOQUET.toString(),
          "-mo",
          "-l/0.5/-d/[0.3;0.3;0.1;0.05]"
        };
  }

  @Override
  public String toString() {
    return "{" + "type=" + type + ", option=" + Arrays.toString(option) + '}';
  }
}
