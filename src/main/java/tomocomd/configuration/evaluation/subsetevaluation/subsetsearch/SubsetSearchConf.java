package tomocomd.configuration.evaluation.subsetevaluation.subsetsearch;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
@Getter
@AllArgsConstructor
public class SubsetSearchConf implements Serializable {
  private final SubsetSearchType subsetSearchType;

  public SubsetSearchConf() {
    subsetSearchType = SubsetSearchType.BEST_FIRST;
  }

  @Override
  public String toString() {
    return "{" + "subsetSearchType=" + subsetSearchType + '}';
  }
}
