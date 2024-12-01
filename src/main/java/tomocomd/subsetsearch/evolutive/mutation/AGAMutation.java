package tomocomd.subsetsearch.evolutive.mutation;

import java.util.Random;
import lombok.Getter;
import tomocomd.configuration.dcs.AHeadEntity;
import tomocomd.configuration.dcs.DCSFactory;
import tomocomd.configuration.subsetsearch.operators.GAMutationConf;
import tomocomd.configuration.subsetsearch.operators.GAMutationType;
import tomocomd.exceptions.AExOpDCSException;

@Getter
public abstract class AGAMutation {

  protected final double prob;
  private final GAMutationType type;
  protected final DCSFactory dcsFactory;

  protected AGAMutation(GAMutationConf conf, DCSFactory dcsFactory) {
    type = conf.getGaMutationType();
    prob = conf.getProb();
    this.dcsFactory = dcsFactory;
  }

  protected abstract void execMutation(AHeadEntity head) throws AExOpDCSException;

  public void mutation(AHeadEntity head) throws AExOpDCSException {
    Random r = new Random(System.currentTimeMillis());
    double rG = r.nextDouble();
    if (rG <= prob) {
      execMutation(head);
    }
  }
}
