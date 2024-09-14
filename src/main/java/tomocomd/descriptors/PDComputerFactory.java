package tomocomd.descriptors;

import tomocomd.configuration.dcs.PDType;
import tomocomd.exceptions.AExOpDCSException;

public class PDComputerFactory {

  private PDComputerFactory() {
    throw new IllegalStateException();
  }

  public static IPDComputer getComputer(PDType type) {
    if (type == PDType.STARTPEP) return new StartpepDescriptorExecutor();
    throw AExOpDCSException.ExceptionType.DCS_EXCEPTION.get("Invalid PD type");
  }
}
