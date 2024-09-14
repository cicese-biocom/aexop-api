package tomocomd.configuration.dcs;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum PDType {
  STARTPEP("StartPEP", "StartPEP");

  private final String name;
  private final String code;
}
