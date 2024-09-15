package tomocomd.configuration.subsetsearch;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import tomocomd.configuration.dcs.AAttributeDCS;
import tomocomd.configuration.dcs.startpep.StartpepDCS;
import tomocomd.configuration.evaluation.subsetevaluation.SubsetEvaluationConfig;
import tomocomd.configuration.subsetsearch.operators.GAResetConf;

@Data
@Builder
@AllArgsConstructor
public class AexopConfig implements Serializable {
  private DCSEvolutiveConfig dcsEvolutiveConfig;
  private List<AAttributeDCS> aAttributeDCSList;
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

  private void initAttributeDCSList() {
    StartpepDCS startpepDCS = new StartpepDCS("No_Classical"); // not classical aggregators
    aAttributeDCSList.add(startpepDCS);

    for (String clas : List.of("AC", "GV", "TS", "ES", "MIC")) {
      StartpepDCS startpepDCSClass = new StartpepDCS(clas);
      startpepDCSClass.getClassicalAggParam().getParams().put(clas, true);
      startpepDCSClass.getClassicalAggParam().getParams().put("", false);
      aAttributeDCSList.add(startpepDCSClass);
    }
  }
}
