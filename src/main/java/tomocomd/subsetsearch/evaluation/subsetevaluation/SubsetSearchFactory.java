/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tomocomd.subsetsearch.evaluation.subsetevaluation;

import tomocomd.configuration.evaluation.subsetevaluation.subsetsearch.SubsetSearchType;
import tomocomd.exceptions.AExOpDCSException;
import weka.attributeSelection.ASSearch;
import weka.attributeSelection.BestFirst;

/**
 * @author potter
 */
public class SubsetSearchFactory {

  private SubsetSearchFactory() {
    throw new IllegalStateException();
  }

  public static ASSearch getSearchMethod(SubsetSearchType type) throws AExOpDCSException {
    if (type == SubsetSearchType.BEST_FIRST) {
      try {
        BestFirst bf = new BestFirst();
        bf.setOptions(new String[] {"-D", "2"});
        return bf;
      } catch (Exception ex) {
        throw AExOpDCSException.ExceptionType.SUBSET_EVALUATE_EXCEPTION.get(
            "Problems initializing Best first search", ex);
      }
    }
    throw new IllegalStateException(String.format("Search method %s type not defined", type));
  }
}
