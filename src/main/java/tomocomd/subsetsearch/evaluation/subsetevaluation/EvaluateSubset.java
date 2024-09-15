/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tomocomd.subsetsearch.evaluation.subsetevaluation;

import java.util.Arrays;
import java.util.BitSet;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import tomocomd.configuration.evaluation.subsetevaluation.SubsetEvaluationConfig;
import tomocomd.data.PopulationInstances;
import tomocomd.exceptions.AExOpDCSException;
import weka.attributeSelection.ASEvaluation;
import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.SubsetEvaluator;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

/**
 * @author potter
 */
@Slf4j
@Data
public class EvaluateSubset extends AttributeSelection implements IEvaluateSubset {

  private static final String MSGBUILDERROR = "Problems building subset evaluator";
  private static final String MSGEVAERROR = "Problems getting attribute index";

  SubsetEvaluationConfig conf;

  public EvaluateSubset(SubsetEvaluationConfig conf) {
    this.conf = conf;
  }

  public PopulationInstances bestSubset(PopulationInstances inst) throws AExOpDCSException {

    long startTime = System.currentTimeMillis();
    ASEvaluation eva =
        SubsetFitnessFactory.getSubsetEvaluator(conf.getSubSetFitnessConf().getType());
    setSearch(
        SubsetSearchFactory.getSearchMethod(conf.getSubsetSearchConf().getSubsetSearchType()));
    setEvaluator(eva);
    setXval(false);

    int[] posA;
    try {
      SelectAttributes(inst);
    } catch (IllegalArgumentException illegal) {
      try {
        log.error(
            "Error searching best subset for a data of {} attributes",
            inst.numAttributes(),
            illegal);
        Remove rem = new Remove();
        rem.setAttributeIndicesArray(new int[] {inst.classIndex()});
        rem.setInvertSelection(true);
        rem.setInputFormat(inst);
        PopulationInstances instRes = new PopulationInstances(Filter.useFilter(inst, rem));
        instRes.setEvaSub(0);
        log.debug(
            "Best subset search performed in {} ms for {} molecular descriptors",
            System.currentTimeMillis() - startTime,
            inst.numAttributes());
        return instRes;
      } catch (Exception ex) {
        throw AExOpDCSException.ExceptionType.SUBSET_EVALUATE_EXCEPTION.get(
            "Error getting best subset", ex);
      }
    } catch (Exception ex) {
      throw AExOpDCSException.ExceptionType.SUBSET_EVALUATE_EXCEPTION.get(MSGBUILDERROR, ex);
    }
    try {
      posA = selectedAttributes();
    } catch (Exception ex) {
      throw AExOpDCSException.ExceptionType.SUBSET_EVALUATE_EXCEPTION.get(MSGEVAERROR, ex);
    }

    double value;
    try {
      value = getMeritFromStringResult();
    } catch (NumberFormatException ex) {
      value = getMeritSubset(inst, posA);
    }

    Arrays.sort(posA);
    Remove rem = new Remove();
    rem.setAttributeIndicesArray(posA);
    rem.setInvertSelection(true);

    try {
      rem.setInputFormat(inst);
      PopulationInstances instRes = new PopulationInstances(Filter.useFilter(inst, rem));
      instRes.setEvaSub(value);
      log.debug(
          "Best subset search performed in {} ms for {} molecular descriptors",
          System.currentTimeMillis() - startTime,
          inst.numAttributes());
      return instRes;
    } catch (Exception ex) {
      throw AExOpDCSException.ExceptionType.SUBSET_EVALUATE_EXCEPTION.get(
          "Error getting best subset", ex);
    }
  }

  private double getMeritFromStringResult() throws NumberFormatException {
    String[] lines = toResultsString().split("\n");
    for (String line : lines) {
      if (line.contains("Merit of best subset found")) {
        String valueS = line.split(":")[1];
        return Double.parseDouble(valueS);
      }
    }
    return Double.NaN;
  }

  public double getMeritSubset(PopulationInstances inst, int[] pos) throws AExOpDCSException {
    ASEvaluation eva =
        SubsetFitnessFactory.getSubsetEvaluator(conf.getSubSetFitnessConf().getType());

    BitSet bitBest = new BitSet(inst.numAttributes());
    if (pos.length == 0) {
      for (int i = 0; i < inst.numAttributes(); i++) {
        if (i != inst.classIndex()) {
          bitBest.set(i);
        }
      }
    } else {
      for (int i : pos) {
        if (i != inst.classIndex()) {
          bitBest.set(i);
        }
      }
    }

    try {
      eva.buildEvaluator(inst);
      return ((SubsetEvaluator) (eva)).evaluateSubset(bitBest);
    } catch (Exception ex) {
      throw AExOpDCSException.ExceptionType.SUBSET_EVALUATE_EXCEPTION.get(
          "Problems evaluating fitness subet", ex);
    }
  }
}
