/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tomocomd.configuration.evaluation.subsetevaluation.subsetfitness;

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
public class SubsetFitnessConf implements Serializable {
  private final SubsetFitnessType type;

  public SubsetFitnessConf() {
    type = SubsetFitnessType.CFS;
  }

  @Override
  public String toString() {
    return "{" + "type=" + type + '}';
  }
}
