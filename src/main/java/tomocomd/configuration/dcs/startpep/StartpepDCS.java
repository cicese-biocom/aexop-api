package tomocomd.configuration.dcs.startpep;

import java.util.Random;
import tomocomd.configuration.dcs.AHeadEntity;
import tomocomd.configuration.dcs.APdDCS;
import tomocomd.configuration.dcs.PDType;
import tomocomd.exceptions.AExOpDCSException;
import tomocomd.utils.Constants;

public class StartpepDCS extends APdDCS {

  private static final String SPACE = ",\n\t ";
  private static final Random rand = new Random();

  protected final ClassicalAggParam classicalAggParam;
  protected final AggregatorsParam aggregatorsParam;
  protected final GroupsParam groupsParam;
  protected final PropertyParam propertyParam;

  protected StartpepDCS() {
    super();
    aggregatorsParam = new AggregatorsParam();
    groupsParam = new GroupsParam();
    propertyParam = new PropertyParam();
    classicalAggParam = new ClassicalAggParam();
  }

  protected StartpepDCS(String name) {
    super(name);
    aggregatorsParam = new AggregatorsParam();
    groupsParam = new GroupsParam();
    propertyParam = new PropertyParam();
    classicalAggParam = new ClassicalAggParam();
  }

  protected StartpepDCS(StartpepDCS startpepDCS) {
    super(startpepDCS.getName());
    aggregatorsParam = startpepDCS.getAggregatorsParam();
    groupsParam = startpepDCS.getGroupsParam();
    propertyParam = startpepDCS.getPropertyParam();
    classicalAggParam = startpepDCS.getClassicalAggParam();
  }

  @Override
  public PDType getType() {
    return PDType.STARTPEP;
  }

  @Override
  public AHeadEntity randomHeading() {
    return null;
  }

  @Override
  public long getSetDim() {
    long size = getGroupsParam().getValues().length;
    size *= getPropertyParam().getValues().length;
    size *= getAggregatorsParam().getValues().length;
    size *= getClassicalAggParam().getValues().length;
    return size;
  }

  @Override
  public String getDesc() {
    return getName()
        + "("
        + getSetDim()
        + " molecular descriptors){"
        + "\n\t Type="
        + getType()
        + SPACE
        + getClassicalAggParam().toString()
        + SPACE
        + getAggregatorsParam().toString()
        + SPACE
        + getGroupsParam().toString()
        + SPACE
        + getPropertyParam().toString()
        + "\n}";
  }

  @Override
  public String[] getValues4Param(String paramName) {
    switch (paramName) {
      case Constants.CLASAGGOPECONST:
        return getClassicalAggParam().getValues();
      case Constants.AGGOPECONST:
        return getAggregatorsParam().getValues();
      case Constants.GROUPSCONST:
        return getGroupsParam().getValues();
      case Constants.PROPCONST:
        return getPropertyParam().getValues();
      default:
        throw AExOpDCSException.ExceptionType.MD_PARAM_EXCEPTION_TYPE.get(
            String.format("Param %s do no exist for MAS MD", paramName));
    }
  }

  public ClassicalAggParam getClassicalAggParam() {
    return classicalAggParam;
  }

  public AggregatorsParam getAggregatorsParam() {
    return aggregatorsParam;
  }

  public GroupsParam getGroupsParam() {
    return groupsParam;
  }

  public PropertyParam getPropertyParam() {
    return propertyParam;
  }
}
