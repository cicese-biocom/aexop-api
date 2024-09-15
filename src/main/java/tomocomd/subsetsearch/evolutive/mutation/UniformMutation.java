package tomocomd.subsetsearch.evolutive.mutation;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import tomocomd.configuration.dcs.AAttributeDCS;
import tomocomd.configuration.dcs.AHeadEntity;
import tomocomd.configuration.dcs.DCSFactory;
import tomocomd.configuration.subsetsearch.operators.GAMutationConf;
import tomocomd.exceptions.AExOpDCSException;

public class UniformMutation extends AGAMutation {

  public UniformMutation(GAMutationConf conf) {
    super(conf);
  }

  @Override
  public void execMutation(AHeadEntity head) throws AExOpDCSException {
    Random r = new Random(System.currentTimeMillis());
    Map<String, String> mapHead = head.parseHead2Map();

    AAttributeDCS dcs = DCSFactory.getDcs(head.getType());

    int cantParam = mapHead.size();
    List<String> params = new LinkedList<>(mapHead.keySet());
    try {
      while (true) {
        int posParam = r.nextInt(cantParam);
        String paramName = params.get(posParam);
        String[] values = dcs.getValues4Param(paramName);
        if (values.length > 1) {
          int posValue = r.nextInt(values.length);
          while (mapHead.get(paramName).equals(values[posValue]))
            posValue = r.nextInt(values.length);
          if (head.setParamValue(paramName, values[posValue])) return;
        }
      }
    } catch (Exception ex) {
      throw AExOpDCSException.ExceptionType.MUTATION_EXCEPTION.get(ex);
    }
  }
}
