package tomocomd.subsetsearch.evaluation.attributeevaluation.fuzzylogic.fuzzymeasures;

import java.util.Arrays;
import lombok.Getter;
import tomocomd.exceptions.AExOpDCSException;

@Getter
public abstract class AFuzzyMeasure {
  protected double[] densitiesValues;

  protected AFuzzyMeasure() {}

  protected AFuzzyMeasure(double[] densitiesValues) {
    this.densitiesValues = Arrays.copyOf(densitiesValues, densitiesValues.length);
  }

  public void setDensitiesValues(double[] densitiesValues) {
    this.densitiesValues = Arrays.copyOf(densitiesValues, densitiesValues.length);
  }

  public abstract void setOptions(String[] opts) throws AExOpDCSException;

  public abstract void buildMeasure() throws AExOpDCSException;

  public abstract double evaluateSubset(Integer[] pos) throws AExOpDCSException;
}
