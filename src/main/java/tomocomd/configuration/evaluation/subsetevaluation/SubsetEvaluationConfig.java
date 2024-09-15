package tomocomd.configuration.evaluation.subsetevaluation;

import java.io.Serializable;
import java.util.Arrays;
import lombok.Builder;
import lombok.Data;
import tomocomd.configuration.evaluation.subsetevaluation.subsetfitness.SubsetFitnessConf;
import tomocomd.configuration.evaluation.subsetevaluation.subsetsearch.SubsetSearchConf;

@Data
@Builder
public class SubsetEvaluationConfig implements Serializable {

  private SubsetSearchConf subsetSearchConf;
  private SubsetFitnessConf subSetFitnessConf;
  private String[] options;

  public SubsetEvaluationConfig() {
    subSetFitnessConf = new SubsetFitnessConf();
    subsetSearchConf = new SubsetSearchConf();
    options = new String[] {};
  }

  public SubsetEvaluationConfig(
      SubsetSearchConf subsetSearchConf, SubsetFitnessConf subSetFitnessConf) {
    this.subsetSearchConf = subsetSearchConf;
    this.subSetFitnessConf = subSetFitnessConf;
    options = new String[] {};
  }

  public SubsetEvaluationConfig(
      SubsetSearchConf subsetSearchConf, SubsetFitnessConf subSetFitnessConf, String[] options) {
    this.subsetSearchConf = subsetSearchConf;
    this.subSetFitnessConf = subSetFitnessConf;
    this.options = new String[options.length];
    System.arraycopy(options, 0, this.options, 0, options.length);
  }

  @Override
  public String toString() {
    return "{"
        + "Search="
        + subsetSearchConf
        + ", Fitness="
        + subSetFitnessConf
        + ", Options="
        + Arrays.toString(options)
        + '}';
  }
}
