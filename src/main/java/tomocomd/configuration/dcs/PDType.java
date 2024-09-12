package tomocomd.configuration.dcs;

public enum PDType {
  STARTPEP("StartPEP", "StartPEP");

  private final String name;
  private final String code;

  PDType(String name, String code) {
    this.name = name;
    this.code = code;
  }
}
