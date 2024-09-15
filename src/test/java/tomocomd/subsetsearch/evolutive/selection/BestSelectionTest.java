package tomocomd.subsetsearch.evolutive.selection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tomocomd.configuration.evaluation.fussylogic.FuzzyIntegralConf;
import tomocomd.configuration.evaluation.fussylogic.FuzzyIntegralType;
import tomocomd.configuration.evaluation.fussylogic.FuzzyMeasureConf;
import tomocomd.configuration.evaluation.fussylogic.FuzzyMeasureType;
import tomocomd.configuration.subsetsearch.operators.GASelectionConfig;
import tomocomd.data.PopulationInstances;
import tomocomd.exceptions.AExOpDCSException;
import tomocomd.io.CSVManage;
import tomocomd.subsetsearch.evaluation.attributeevaluation.AttributeEvaluationChoquet;

@ExtendWith(MockitoExtension.class)
class BestSelectionTest {
  BestSelection bestSelection;
  @Mock private FuzzyMeasureConf fuzzyMeasureConf;

  @Mock private FuzzyIntegralConf fuzzyIntegralConf;

  @InjectMocks AttributeEvaluationChoquet choquet;

  @Test
  void getOneParent() throws AExOpDCSException {
    PopulationInstances data = new PopulationInstances(CSVManage.loadCSV("csv/classData.csv"));
    data.setClassIndex(0);
    when(fuzzyMeasureConf.getType()).thenReturn(FuzzyMeasureType.Q);
    when(fuzzyMeasureConf.getOptions())
        .thenReturn(new String[] {"-l", "0.5", "-d", "[0.3;0.3;0.1;0.05]"});
    when(fuzzyIntegralConf.getType()).thenReturn(FuzzyIntegralType.CHOQUET);
    when(fuzzyIntegralConf.getConfMeas()).thenReturn(fuzzyMeasureConf);

    choquet = new AttributeEvaluationChoquet(fuzzyIntegralConf);
    choquet.buildEvaluator(data);

    GASelectionConfig conf = new GASelectionConfig();
    conf.setCant(5);
    bestSelection = new BestSelection(conf);
    bestSelection.build(data);
    assertEquals(10, bestSelection.getOneParent());
  }
}
