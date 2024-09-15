/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tomocomd.subsetsearch.evolutive.crossover;

import java.util.*;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tomocomd.configuration.dcs.AHeadEntity;
import tomocomd.configuration.dcs.HeadFactory;
import tomocomd.configuration.subsetsearch.operators.GACrossoverConf;
import tomocomd.configuration.subsetsearch.operators.GACrossoverType;
import tomocomd.exceptions.AExOpDCSException;

/**
 * @author Potter
 */
@Getter
public abstract class AGACrossoverOperation {

  protected final double prob;
  private final GACrossoverType type;

  static final Logger LOGGER = LogManager.getLogger(AGACrossoverOperation.class);

  protected AGACrossoverOperation(GACrossoverConf conf) {
    type = conf.getType();
    prob = conf.getProb();
  }

  public abstract List<AHeadEntity> crossover(AHeadEntity a1, AHeadEntity a2)
      throws AExOpDCSException;

  public List<AHeadEntity> makeCrossover(AHeadEntity headP1, AHeadEntity headP2)
      throws AExOpDCSException {
    List<AHeadEntity> children = Arrays.asList(headP1, headP2);
    Random random = new Random(System.currentTimeMillis());
    double probCross = random.nextDouble();
    if (probCross <= prob) {
      long crossStartTime = System.currentTimeMillis();
      LOGGER.debug(
          "Starting crossover step for {} family with probability {}", headP1.getType(), probCross);
      children = crossover(headP1, headP2);
      LOGGER.debug(
          "Completed crossover step for {} family in {} ms",
          headP1.getType(),
          System.currentTimeMillis() - crossStartTime);
    }
    return children;
  }

  protected List<AHeadEntity> getaHeadEntities(
      AHeadEntity a1, AHeadEntity a2, Map<String, String> hA1, Map<String, String> hA2) {
    try {
      AHeadEntity h1 = HeadFactory.getHead(a1.getType());
      h1.parseMap2Head(hA1);
      AHeadEntity h2 = HeadFactory.getHead(a2.getType());
      h2.parseMap2Head(hA2);
      List<AHeadEntity> heads = new LinkedList<>();
      heads.add(h1);
      heads.add(h2);
      return heads;
    } catch (Exception ex) {
      throw AExOpDCSException.ExceptionType.CROSSOVER_EXCEPTION.get(
          "Error executing Uniform crossover", ex);
    }
  }
}
