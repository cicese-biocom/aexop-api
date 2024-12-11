package tomocomd.descriptors;

import tomocomd.configuration.dcs.ComputerType;

public interface AttributeComputerFactory {

  IAttributeComputer getComputer(ComputerType type);
}
