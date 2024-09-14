package tomocomd.descriptors;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Set;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tomocomd.configuration.dcs.PDType;
import tomocomd.data.PopulationInstances;
import tomocomd.exceptions.AExOpDCSException;

class StartpepDescriptorExecutorTest {

  StartpepDescriptorExecutor startpepDescriptorExecutor;
  Path filePath;

  @SneakyThrows
  @BeforeEach
  void setUp() {
    startpepDescriptorExecutor = new StartpepDescriptorExecutor();
    ClassLoader classLoader = getClass().getClassLoader();
    filePath = Paths.get(Objects.requireNonNull(classLoader.getResource("seq.fasta")).toURI());
  }

  @Test
  void compute() {
    Set<String> pdSet = Set.of("MIC_S_T_ptt", "TIC_T_ptt", "ES_TIC_T_ptt");
    PopulationInstances data = startpepDescriptorExecutor.compute(pdSet, filePath.toString());
    assertNotNull(data);
    assertEquals(3, data.numAttributes());
    assertEquals(4, data.numInstances());
    assertArrayEquals(
        new double[] {1.1298738344298023, 151.31881626063642, 226.47733175670794},
        data.instance(0).toDoubleArray());
  }

  @Test
  void computeFailFile() {
    Set<String> pdSet = Set.of("MIC_S_T_ptt", "TIC_T_ptt", "ES_TIC_T_ptt");
    try {
      startpepDescriptorExecutor.compute(pdSet, filePath.toString() + "fail");
    } catch (AExOpDCSException e) {
      assertEquals("Error reading the sequences file", e.getMessage());
      assertEquals(AExOpDCSException.ExceptionType.READING_SEQ_FILE_EXCEPTION_TYPE, e.getType());
    }
  }

  @Test
  void getType() {
    assertEquals(PDType.STARTPEP, startpepDescriptorExecutor.getType());
  }
}
