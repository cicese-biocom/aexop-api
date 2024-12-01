/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tomocomd.subsetsearch.evolutive.crossover;

import java.util.*;
import tomocomd.configuration.dcs.AHeadEntity;
import tomocomd.configuration.dcs.HeadFactory;
import tomocomd.configuration.subsetsearch.operators.GACrossoverConf;
import tomocomd.exceptions.AExOpDCSException;

/**
 * @author Potter
 */
public class UniformCrossover extends AGACrossoverOperation {

  public UniformCrossover(GACrossoverConf conf, HeadFactory headFactory) {
    super(conf, headFactory);
  }

  @Override
  public List<AHeadEntity> crossover(AHeadEntity a1, AHeadEntity a2) throws AExOpDCSException {
    Random r = new Random(System.currentTimeMillis());
    if (a1.getType() != a2.getType()) {
      throw AExOpDCSException.ExceptionType.CROSSOVER_EXCEPTION.get(
          String.format("The MD heads have to be the same type, %s != %s", a1, a2));
    }

    // get de params and values from heads
    String value1;
    String value2;
    Map<String, String> pA1 = a1.parseHead2Map();
    Map<String, String> pA2 = a2.parseHead2Map();
    Map<String, String> hA1 = new HashMap<>();
    Map<String, String> hA2 = new HashMap<>();

    // get all the params actives in both heads
    Set<String> keys = new LinkedHashSet<>(pA1.keySet());
    Set<String> keys2 = pA2.keySet();
    keys.addAll(keys2);

    // crossover function
    for (String key : keys) {
      value1 = pA1.getOrDefault(key, "");
      value2 = pA2.getOrDefault(key, "");
      if (r.nextDouble() > 0.5) {
        hA1.put(key, value2);
        hA2.put(key, value1);
      } else {
        hA1.put(key, value1);
        hA2.put(key, value2);
      }
    }

    // initialize the children and fix errors
    return getaHeadEntities(a1, a2, hA1, hA2);
  }
}
