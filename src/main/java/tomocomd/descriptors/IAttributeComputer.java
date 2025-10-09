package tomocomd.descriptors;

import java.io.Serializable;
import java.util.Set;
import tomocomd.configuration.dcs.ComputerType;
import tomocomd.data.PopulationInstances;

public interface IAttributeComputer extends Serializable {
  PopulationInstances compute(Set<String> headSet, String filePath);

  ComputerType getType();
}
