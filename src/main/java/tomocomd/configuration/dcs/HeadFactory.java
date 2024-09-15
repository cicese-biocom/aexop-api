package tomocomd.configuration.dcs;

import tomocomd.configuration.dcs.startpep.StartpepHeadEntity;

public class HeadFactory {

  private HeadFactory() {}

  public static AHeadEntity getHead(PDType type) {

    if (type == null) throw new IllegalArgumentException("Head type not supported: " + type);

    if (type == PDType.STARTPEP) {
      return new StartpepHeadEntity(type);
    }
    throw new IllegalArgumentException("Head type not supported: " + type);
  }
}
