package tomocomd.configuration.dcs;

import tomocomd.configuration.dcs.startpep.StartpepDCS;

public class DCSFactory {

  private DCSFactory() {}

  public static AAttributeDCS getDcs(PDType type) {
    if (type == null) throw new IllegalArgumentException("Null DCS not allowed");
    if (type == PDType.STARTPEP) {
      return new StartpepDCS();
    }
    throw new IllegalArgumentException("DCS type not supported: " + type);
  }
}
