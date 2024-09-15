package tomocomd.subsetsearch.evolutive.mutation;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import tomocomd.configuration.dcs.AAttributeDCS;
import tomocomd.configuration.dcs.AHeadEntity;
import tomocomd.configuration.dcs.DCSFactory;
import tomocomd.configuration.dcs.PDType;
import tomocomd.configuration.subsetsearch.operators.GAMutationConf;
import tomocomd.configuration.subsetsearch.operators.GAMutationType;

class UniformMutationTest {
  @Test
  void mutation() {
    AAttributeDCS head = DCSFactory.getDcs(PDType.STARTPEP);
    AHeadEntity initialH = head.randomHeading();
    String headIni = initialH.toString();

    UniformMutation uniformMutation =
        new UniformMutation(new GAMutationConf(GAMutationType.UNIFORM, 1));
    uniformMutation.execMutation(initialH);
    assertNotEquals(headIni, initialH.toString());
  }
}
