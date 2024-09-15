/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tomocomd.subsetsearch.evolutive.crossover;

import java.util.*;
import tomocomd.configuration.dcs.AHeadEntity;
import tomocomd.configuration.subsetsearch.operators.GACrossoverConf;
import tomocomd.exceptions.AExOpDCSException;
import tomocomd.utils.Statistics;

/**
 * @author Potter
 */
public class HuxCrossover extends AGACrossoverOperation {

  public HuxCrossover(GACrossoverConf conf) {
    super(conf);
  }

  @Override
  public List<AHeadEntity> crossover(AHeadEntity a1, AHeadEntity a2) throws AExOpDCSException {

    if (a1.getType() != a2.getType()) {
      throw AExOpDCSException.ExceptionType.CROSSOVER_EXCEPTION.get(
          String.format("The MD heads have to be the same type, %s != %s", a1, a2));
    }

    Random r = new Random(System.currentTimeMillis());
    // get de params and values from heads
    String value1;
    String value2;

    Map<String, String> pA1 = a1.parseHead2Map();
    Map<String, String> pA2 = a2.parseHead2Map();
    Map<String, String> hA1 = new HashMap<>();
    Map<String, String> hA2 = new HashMap<>();

    // get de head with max number of params
    Set<String> keys = new LinkedHashSet<>(pA1.keySet());
    Set<String> keys2 = pA2.keySet();
    keys.addAll(keys2);

    int d;
    try {
      d = Statistics.hammingDistance(a1, a2);
    } catch (Exception e) {
      throw AExOpDCSException.ExceptionType.CROSSOVER_EXCEPTION.get(
          "Error computing hamming distance", e);
    }
    if (d < 2) {
      return new LinkedList<>();
    }
    d /= 2;

    boolean flag = r.nextBoolean();
    for (String key : keys) {
      value1 = pA1.getOrDefault(key, "");
      value2 = pA2.getOrDefault(key, "");

      if (value1.equals(value2)) {
        hA1.put(key, value1);
        hA2.put(key, value2);
      } else {
        if (d > 0 && flag) {
          d--;
          hA1.put(key, value2);
          hA2.put(key, value1);
        } else {
          hA1.put(key, value1);
          hA2.put(key, value2);
        }
        flag = !flag;
      }
    }

    // initialize the children and fix error
    return getaHeadEntities(a1, a2, hA1, hA2);
  }
}
