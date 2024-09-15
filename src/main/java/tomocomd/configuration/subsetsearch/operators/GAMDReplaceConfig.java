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
public class GAMDReplaceConfig implements Serializable {

  private GAMDReplaceType type;

  public GAMDReplaceConfig() {
    type = GAMDReplaceType.PARENT;
  }

  @Override
  public String toString() {
    return "{" + "type=" + type + '}';
  }
}
