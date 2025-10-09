package tomocomd.configuration.dcs;

import java.io.Serializable;

public interface HeadFactory extends Serializable {

  AHeadEntity getHead(AttributeType type);
}
