package tomocomd.configuration.evaluation.subsetevaluation.subsetsearch;

import lombok.Getter;

@Getter
public enum SubsetSearchType {
  BEST_FIRST("Best first"),
  MEAN_STD_REMOVE("Remove MD with quality lower than mean + std");

  private final String value;

  SubsetSearchType(String value) {
    this.value = value;
  }

  public static SubsetSearchType getEnum(String value) {
    if (value.equals("Best first")) return BEST_FIRST;
    else if (value.equals("Remove MD with quality lower than mean + std")) return MEAN_STD_REMOVE;
    throw new IllegalStateException("String value do not match with any Subset search type");
  }

  @Override
  public String toString() {
    return this.getValue();
  }
}
