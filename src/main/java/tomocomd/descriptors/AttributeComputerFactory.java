package tomocomd.descriptors;

import tomocomd.configuration.dcs.AttributeType;

public interface AttributeComputerFactory {

  IAttributeComputer getComputer(AttributeType type);
}
