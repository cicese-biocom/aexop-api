package tomocomd.configuration.dcs;

public interface DCSFactory {

  AAttributeDCS getDcs(AttributeType type);
}
