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
public class FuzzyMeasureConf implements Serializable {

  private FuzzyMeasureType type;
  private String[] options;

  public FuzzyMeasureConf() {
    getDefaultsValues();
  }

  private void getDefaultsValues() {
    type = FuzzyMeasureType.Q;
    options = new String[] {"-l", "0.5", "-d", "[0.3;0.3;0.1;0.05]"};
  }

  public FuzzyMeasureConf(FuzzyMeasureType type, String[] options) {
    this.type = type;
    this.options = options;
  }
}
