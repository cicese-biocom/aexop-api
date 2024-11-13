/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tomocomd.subsetsearch.evaluation.subsetevaluation;

import tomocomd.configuration.evaluation.subsetevaluation.subsetfitness.SubsetFitnessType;
import weka.attributeSelection.ASEvaluation;

/**
 * @author potter
 */
public class SubsetFitnessFactory {

  private SubsetFitnessFactory() {
    throw new IllegalStateException();
  }

  public static ASEvaluation getSubsetEvaluator(SubsetFitnessType type) {
    if (type == SubsetFitnessType.CFS) {
      return new CfsSubsetEvalDiscretePrecision();
    }
    throw new IllegalStateException(String.format("Merit subset function %s not defined", type));
  }
}
