package tomocomd.utils;

import java.util.LinkedHashMap;
import java.util.Map;
import tomocomd.exceptions.AExOpDCSException;

public class ParseOptions {
  private ParseOptions() {
    throw new IllegalStateException();
  }

  public static String getOption(String flag, String[] opts) throws AExOpDCSException {
    try {
      for (int i = 0; i < opts.length; i += 2) {
        if (opts[i].equals(flag)) {
          return opts[i + 1];
        }
      }
    } catch (Exception ex) {
      throw AExOpDCSException.ExceptionType.PARSE_EXCEPTION.get("Problems parsing list of options");
    }
    throw AExOpDCSException.ExceptionType.PARSE_EXCEPTION.get(
        String.format("Option name %s do not exist", flag));
  }

  public static Map<String, String> getOption(String[] opts) throws AExOpDCSException {
    Map<String, String> optMap = new LinkedHashMap<>();

    try {
      for (int i = 0; i < opts.length; i += 2) {
        optMap.put(opts[i], opts[i + 1]);
      }
      return optMap;
    } catch (Exception ex) {
      throw AExOpDCSException.ExceptionType.PARSE_EXCEPTION.get(
          "Problems parsing list of options", ex);
    }
  }
}
