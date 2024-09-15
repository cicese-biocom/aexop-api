/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tomocomd.configuration.evaluation.fussylogic;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

/**
 * @author potter
 */
@Data
@Builder
public class FuzzyIntegralConf implements Serializable {

  private FuzzyIntegralType type;
  private FuzzyMeasureConf confMeas;

  public FuzzyIntegralConf() {
    getDefaultsValues();
  }

  private void getDefaultsValues() {
    type = FuzzyIntegralType.CHOQUET;
    confMeas = new FuzzyMeasureConf();
  }

  public FuzzyIntegralConf(FuzzyIntegralType type, FuzzyMeasureConf confMeas) {
    this.type = type;
    this.confMeas = confMeas;
  }
}
