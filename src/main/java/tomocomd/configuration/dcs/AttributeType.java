package tomocomd.configuration.dcs;

import java.io.Serializable;

// Define una interfaz para los atributos
public interface AttributeType extends Serializable {
  ComputerType getComputerType();

  String getCode();
}
