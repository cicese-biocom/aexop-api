package tomocomd.exceptions;

import lombok.Getter;

@Getter
public class AExOpDCSException extends RuntimeException {
  private final ExceptionType type;

  public AExOpDCSException(ExceptionType type) {
    super(type.getMessage());
    this.type = type;
  }

  public AExOpDCSException(ExceptionType type, Throwable cause) {
    super(type.getMessage(), cause);
    this.type = type;
  }

  public AExOpDCSException(ExceptionType type, Throwable cause, String message) {
    super(type.formatMessage(message), cause);
    this.type = type;
  }

  public AExOpDCSException(ExceptionType type, String message) {
    super(type.formatMessage(message));
    this.type = type;
  }

  @Getter
  public enum ExceptionType {
    DCS_EXCEPTION("Invalid DCS"),
    MD_PARAM_EXCEPTION_TYPE("Unknown param name for a DCS"),
    MD_EVALUATION_FUNCTION_EXCEPTION("Error in evaluation function"),
    STATISTICS_EXCEPTION("Error in evaluation function"),
    FUZZY_MEASURE_EXCEPTION("Error in evaluation fuzzy measure"),
    PARSE_EXCEPTION("Error parsing"),
    FUZZY_INTEGRAL_EXCEPTION("Error in evaluating fuzzy integral"),
    SUBSET_EVALUATE_EXCEPTION("Error in evaluating subset"),
    FILTER_EXCEPTION("Error Filtering data"),
    REPLACE_EXCEPTION("Error replacing populations"),
    CROSSOVER_EXCEPTION("Error in crossover"),
    MUTATION_EXCEPTION("Error executing mutation operator"),
    SELECTION_EXCEPTION("Error selecting MDs"),
    CSV_FILE_WRITING_EXCEPTION("Error writing csv file"),
    CSV_FILE_LOADING_EXCEPTION("Error reading csv file"),
    DCS_EVOLUTION_EXCEPTION("Error Executing evolution process for some DCS"),
    AEXOPDCS_EXCEPTION("Error Executing evolution process"),
    METRIC_EXCEPTION("Error initializing resource metrics"),
    READING_SEQ_FILE_EXCEPTION_TYPE("Error reading the input file");

    private final String message;

    ExceptionType(String message) {
      this.message = message;
    }

    public String formatMessage(String message) {
      return String.format("%s: %s", this.message, message);
    }

    public AExOpDCSException get() {
      return new AExOpDCSException(this);
    }

    public AExOpDCSException get(String message) {
      return new AExOpDCSException(this, message);
    }

    public AExOpDCSException get(Throwable cause) {
      return new AExOpDCSException(this, cause);
    }

    public AExOpDCSException get(String message, Throwable cause) {
      return new AExOpDCSException(this, cause, message);
    }
  }
}
