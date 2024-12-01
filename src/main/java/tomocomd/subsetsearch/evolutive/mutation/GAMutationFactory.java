package tomocomd.subsetsearch.evolutive.mutation;

import tomocomd.configuration.dcs.DCSFactory;
import tomocomd.configuration.subsetsearch.operators.GAMutationConf;
import tomocomd.configuration.subsetsearch.operators.GAMutationType;
import tomocomd.exceptions.AExOpDCSException;

public class GAMutationFactory {

  private GAMutationFactory() {
    throw new IllegalStateException();
  }

  public static AGAMutation getMutation(GAMutationConf conf, DCSFactory dcsFactory)
      throws AExOpDCSException {
    if (conf.getGaMutationType() == GAMutationType.UNIFORM) {
      return new UniformMutation(conf, dcsFactory);
    }
    throw AExOpDCSException.ExceptionType.MUTATION_EXCEPTION.get(
        String.format("Mutation %s do not defined", conf.getGaMutationType()));
  }
}
