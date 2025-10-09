package tomocomd.descriptors;

import java.io.Serializable;
import tomocomd.configuration.dcs.ComputerType;

public interface AttributeComputerFactory extends Serializable {

  IAttributeComputer getComputer(ComputerType type);
}
