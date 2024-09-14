package tomocomd.descriptors;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import tomocomd.configuration.dcs.PDType;
import tomocomd.exceptions.AExOpDCSException;

class PDComputerFactoryTest {

  PDComputerFactory pdComputerFactory;

  @Test
  void getComputer() {
    assertEquals(PDType.STARTPEP, pdComputerFactory.getComputer(PDType.STARTPEP).getType());
  }

  @Test
  void getComputerError() {
    try {
      pdComputerFactory.getComputer(PDType.STARTPEP);
    } catch (AExOpDCSException e) {
      assertEquals("Invalid PD type", e.getMessage());
    }
  }
}
