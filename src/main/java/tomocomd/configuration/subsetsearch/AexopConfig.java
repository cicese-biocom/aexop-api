package tomocomd.configuration.subsetsearch;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import tomocomd.configuration.dcs.AAttributeDCS;
import tomocomd.configuration.evaluation.subsetevaluation.SubsetEvaluationConfig;
import tomocomd.configuration.subsetsearch.operators.GAResetConf;

@Data
@AllArgsConstructor
public abstract class AexopConfig implements Serializable {
  private DCSEvolutiveConfig dcsEvolutiveConfig;
  protected List<AAttributeDCS> aAttributeDCSList;
  protected GAResetConf gaResetConf;
  private SubsetEvaluationConfig subsetEva;
  private Integer numIter;
  private Boolean coop;

  public AexopConfig() {
    dcsEvolutiveConfig = new DCSEvolutiveConfig();

    aAttributeDCSList = new LinkedList<>();
    initAttributeDCSList();

    subsetEva = new SubsetEvaluationConfig();
    gaResetConf = new GAResetConf();

    numIter = 10000;
    coop = true;
  }

  @Override
  public String toString() {
    String familiesString =
        aAttributeDCSList.stream()
            .map(AAttributeDCS::getDesc)
            .collect(Collectors.joining(",\n\t\t", "\t\t", ""));
    return "Genetic algorithm{"
        + "\n\t Number of iteration="
        + getNumIter()
        + ",\n\t Fitness function="
        + getSubsetEva()
        + ",\n\t Reset population="
        + getGaResetConf()
        + ",\n\t Cooperative="
        + getCoop()
        + ",\n\t Evolutive setting: "
        + getDcsEvolutiveConfig()
        + ",\n\t DCS=[\n"
        + familiesString
        + "\n\t]\n}";
  }

  protected abstract void initAttributeDCSList();
}
