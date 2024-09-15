package tomocomd.configuration.evaluation.subsetevaluation;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import tomocomd.configuration.evaluation.subsetevaluation.subsetfitness.SubsetFitnessConf;
import tomocomd.configuration.evaluation.subsetevaluation.subsetfitness.SubsetFitnessType;
import tomocomd.configuration.evaluation.subsetevaluation.subsetsearch.SubsetSearchConf;
import tomocomd.configuration.evaluation.subsetevaluation.subsetsearch.SubsetSearchType;

@ExtendWith(MockitoExtension.class)
class SubsetEvaluationConfigTest {
  SubsetEvaluationConfig subsetEvaluationConfig;

  @BeforeEach
  void setUp() {
    subsetEvaluationConfig =
        SubsetEvaluationConfig.builder()
            .subsetSearchConf(
                SubsetSearchConf.builder().subsetSearchType(SubsetSearchType.BEST_FIRST).build())
            .subSetFitnessConf(SubsetFitnessConf.builder().type(SubsetFitnessType.CFS).build())
            .options(new String[] {})
            .build();
  }

  @Test
  void testToString() {
    assertEquals(getStringOptNull(), subsetEvaluationConfig.toString());
  }

  @Test
  void setOptions() {
    subsetEvaluationConfig.setOptions(new String[] {"-D", "2"});
    assertEquals(getStringOpt(), subsetEvaluationConfig.toString());
  }

  String getStringOptNull() {
    return "{Search={subsetSearchType=Best first}, Fitness={type=Merit from Correlation subset}, Options=[]}";
  }

  String getStringOpt() {
    return "{Search={subsetSearchType=Best first}, Fitness={type=Merit from Correlation subset}, Options=[-D, 2]}";
  }
}
