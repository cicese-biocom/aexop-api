package tomocomd.utils;

import java.util.*;
import java.util.stream.Collectors;

public class SortInstancesByAttribute {

  private SortInstancesByAttribute() {
    throw new IllegalStateException("SortInstancesByAttribute class");
  }

  public static List<Integer> sortAndGetPos(double[] v, boolean reverse) {
    Map<Integer, Double> unorder = new LinkedHashMap<>();
    for (int i = 0; i < v.length; i++) {
      unorder.put(i, v[i]);
    }

    return sortByAttValue(unorder, reverse);
  }

  public static List<Integer> sortByAttValue(Map<Integer, Double> valuesByPos, boolean reverse) {

    Comparator<Double> comparator = reverse ? Comparator.reverseOrder() : Comparator.naturalOrder();
    Map<Integer, Double> sortedValues =
        valuesByPos.entrySet().stream()
            .sorted(Map.Entry.comparingByValue(comparator))
            .collect(
                Collectors.toMap(
                    Map.Entry::getKey,
                    Map.Entry::getValue,
                    (oldValue, newValue) -> oldValue,
                    LinkedHashMap::new));

    return new LinkedList<>(sortedValues.keySet());
  }
}
