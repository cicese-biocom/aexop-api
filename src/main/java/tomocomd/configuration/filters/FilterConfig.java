/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tomocomd.configuration.filters;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author Potter
 */
@Data
@Builder
@AllArgsConstructor
public class FilterConfig implements Serializable {
  private FilterType type;
  private String[] options;

  static String[][] values =
      new String[][] {
        {"NAN", "1"},
        {"CORR", "3"},
        {"KUR", "1"},
        {"SE", "2"},
        {"R2", "3"},
      };
  private static Map<FilterType, Integer> typesByLevel =
      Stream.of(values)
          .collect(
              Collectors.collectingAndThen(
                  Collectors.toMap(
                      data -> FilterType.valueOf(data[0]), data -> Integer.parseInt(data[1])),
                  Collections::<FilterType, Integer>unmodifiableMap));

  public Integer getLevel() {
    return typesByLevel.get(type);
  }

  @Override
  public String toString() {
    return "{" + "type=" + type + ", options=" + Arrays.toString(options) + '}';
  }
}
