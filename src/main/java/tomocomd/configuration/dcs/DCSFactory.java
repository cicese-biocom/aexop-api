package tomocomd.configuration.dcs;

import java.io.Serializable;

public interface DCSFactory extends Serializable {

  AAttributeDCS getDcs(AttributeType type);
}
