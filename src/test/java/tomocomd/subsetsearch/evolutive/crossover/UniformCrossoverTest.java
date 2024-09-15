package tomocomd.subsetsearch.evolutive.crossover;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tomocomd.configuration.dcs.AHeadEntity;
import tomocomd.configuration.dcs.HeadFactory;
import tomocomd.configuration.dcs.PDType;
import tomocomd.configuration.subsetsearch.operators.GACrossoverConf;
import tomocomd.configuration.subsetsearch.operators.GACrossoverType;
import tomocomd.exceptions.AExOpDCSException;

class UniformCrossoverTest {

  private UniformCrossover uniformCrossover;

  @BeforeEach
  void setUp() {
    uniformCrossover = new UniformCrossover(new GACrossoverConf(GACrossoverType.UNIFORM, 1));
  }

  @Test
  void crossover() throws AExOpDCSException {
    AHeadEntity head1 = HeadFactory.getHead(PDType.STARTPEP);
    AHeadEntity head2 = HeadFactory.getHead(PDType.STARTPEP);

    head1.setFromString("MIC_S_T_gcp1");
    head2.setFromString("TIC_A_ptt");

    List<AHeadEntity> children = uniformCrossover.makeCrossover(head1, head2);

    assertEquals(2, children.size());
    assertNotEquals(children.get(0).toString(), head1.toString());
    assertNotEquals(children.get(1).toString(), head2.toString());
  }
}
