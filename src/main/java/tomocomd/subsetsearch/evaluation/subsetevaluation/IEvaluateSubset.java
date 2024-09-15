package tomocomd.subsetsearch.evaluation.subsetevaluation;

import tomocomd.data.PopulationInstances;
import tomocomd.exceptions.AExOpDCSException;

public interface IEvaluateSubset {

  public abstract PopulationInstances bestSubset(PopulationInstances inst) throws AExOpDCSException;
}
