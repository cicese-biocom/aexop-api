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
    READING_SEQ_FILE_EXCEPTION_TYPE("Error reading the sequences file");

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
