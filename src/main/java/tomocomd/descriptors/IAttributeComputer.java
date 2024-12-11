package tomocomd.descriptors;

import java.util.Set;
import tomocomd.configuration.dcs.ComputerType;
import tomocomd.data.PopulationInstances;

public interface IAttributeComputer {
  PopulationInstances compute(Set<String> headSet, String filePath);

  ComputerType getType();
}
