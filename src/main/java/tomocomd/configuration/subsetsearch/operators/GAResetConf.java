/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tomocomd.configuration.subsetsearch.operators;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author Potter
 */
@Data
@Builder
@AllArgsConstructor
public class GAResetConf implements Serializable {
  private Integer numIter;

  public GAResetConf() {
    numIter = 2000;
  }

  @Override
  public String toString() {
    return "{" + "Reset population each " + numIter + " iterations" + '}';
  }
}
