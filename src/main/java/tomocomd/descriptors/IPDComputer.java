package tomocomd.descriptors;

import java.util.Set;
import tomocomd.configuration.dcs.PDType;
import tomocomd.data.PopulationInstances;

public interface IPDComputer {
  PopulationInstances compute(Set<String> pdSet, String seqFilePath);

  PDType getType();
}
