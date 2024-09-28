package tomocomd.configuration.subsetsearch;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import tomocomd.configuration.evaluation.mdevaluation.AttributeEvaluationConf;
import tomocomd.configuration.filters.FilterConfig;
import tomocomd.configuration.filters.FilterType;
import tomocomd.configuration.subsetsearch.operators.GACrossoverConf;
import tomocomd.configuration.subsetsearch.operators.GAMDReplaceConfig;
import tomocomd.configuration.subsetsearch.operators.GAMutationConf;
import tomocomd.configuration.subsetsearch.operators.GASelectionConfig;

@Data
@Builder
@AllArgsConstructor
public class DCSEvolutiveConfig implements Serializable {

  protected Integer numDesc;
  protected Boolean incest;

  // GA operators
  private List<FilterConfig> filtersConfig;
  private GASelectionConfig selConf;
  private GACrossoverConf crossConf;
  private GAMutationConf mutConf;
  private GAMDReplaceConfig replaceSubConf;
  private AttributeEvaluationConf attConf;

  public DCSEvolutiveConfig() {
    this.numDesc = 100;
    incest = true;
    filtersConfig = new LinkedList<>();
    filtersConfig.add(new FilterConfig(FilterType.NAN, new String[] {"-t", "DELETE"}));
    filtersConfig.add(new FilterConfig(FilterType.SE, new String[] {"-t", "0.15"}));

    this.selConf = new GASelectionConfig();
    this.crossConf = new GACrossoverConf();
    this.replaceSubConf = new GAMDReplaceConfig();
    this.attConf = new AttributeEvaluationConf();
    this.mutConf = new GAMutationConf();
  }

  public String toString() {
    String filtersConfigString =
        filtersConfig.stream().map(String::valueOf).collect(Collectors.joining(",", "[", "]"));

    return "GA Algorithm search configuration{"
        + "\n\t\t\t\t Population size="
        + getNumDesc()
        + ",\n\t\t\t\t Incest="
        + getIncest()
        + ",\n\t\t\t\t Filters="
        + filtersConfigString
        + ",\n\t\t\t\t Fitness function="
        + getAttConf()
        + ",\n\t\t\t\t Selection="
        + getSelConf()
        + ",\n\t\t\t\t Crossover="
        + getCrossConf()
        + ",\n\t\t\t\t Mutation="
        + getMutConf()
        + ",\n\t\t\t\t Replace md="
        + getReplaceSubConf()
        + "\n\t}";
  }
}
