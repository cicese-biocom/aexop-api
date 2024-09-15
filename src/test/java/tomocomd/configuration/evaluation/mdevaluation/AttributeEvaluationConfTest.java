package tomocomd.configuration.evaluation.mdevaluation;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import tomocomd.configuration.evaluation.fussylogic.FuzzyIntegralType;
import tomocomd.configuration.evaluation.fussylogic.FuzzyMeasureType;

@ExtendWith(MockitoExtension.class)
class MDNameEvaluationConfTest {

  AttributeEvaluationConf attributeEvaluationConf;

  @BeforeEach
  void setUp() {
    attributeEvaluationConf =
        AttributeEvaluationConf.builder()
            .type(AttributeEvaluationType.CHOQUET)
            .option(
                new String[] {
                  "-m",
                  FuzzyMeasureType.Q.toString(),
                  "-i",
                  FuzzyIntegralType.CHOQUET.toString(),
                  "-mo",
                  "-l/0.5/-d/[0.3;0.3;0.1;0.05]"
                })
            .build();
  }

  @Test
  void testToString() {
    assertEquals(
        "{type=Choquet integral, option=[-m, Q-measure, -i, Choquet, -mo, -l/0.5/-d/[0.3;0.3;0.1;0.05]]}",
        attributeEvaluationConf.toString());
  }

  @Test
  void testChangeToString() {
    attributeEvaluationConf.setType(AttributeEvaluationType.SE);
    attributeEvaluationConf.setOption(new String[] {});
    assertEquals("{type=Shannon entropy, option=[]}", attributeEvaluationConf.toString());
  }
}
