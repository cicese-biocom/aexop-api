/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tomocomd.configuration.subsetsearch.operators;

import java.io.Serializable;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author Potter
 */
@Data
@Builder
@AllArgsConstructor
public class GASelectionConfig implements Serializable {

  private Integer cant;
  private GASelectionType type;
  private String[] options;

  public GASelectionConfig() {
    cant = 20;
    type = GASelectionType.TOURNAMENT;
    options = new String[] {"-s", "5"};
  }

  @Override
  public String toString() {
    return "{" + "cant=" + cant + ", type=" + type + ", options=" + Arrays.toString(options) + '}';
  }
}
