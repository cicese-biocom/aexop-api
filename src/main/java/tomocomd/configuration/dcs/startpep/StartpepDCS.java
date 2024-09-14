package tomocomd.configuration.dcs.startpep;

import java.util.Random;
import lombok.AllArgsConstructor;
import lombok.Getter;
import tomocomd.configuration.dcs.AHeadEntity;
import tomocomd.configuration.dcs.APdDCS;
import tomocomd.configuration.dcs.PDType;
import tomocomd.exceptions.AExOpDCSException;
import tomocomd.md.HeaderValidator;
import tomocomd.utils.Constants;

@Getter
@AllArgsConstructor
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

  @Override
  public PDType getType() {
    return PDType.STARTPEP;
  }

  @Override
  public AHeadEntity randomHeading() {
    StartpepHeadEntity head = getValidateHead();
    while (!HeaderValidator.validateHeader(head.toString())) {
      head = getValidateHead();
    }
    return head;
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

  private StartpepHeadEntity getValidateHead() {
    String classAgreOpe =
        getClassicalAggParam().getValues()[rand.nextInt(getClassicalAggParam().getValues().length)];
    String agreOpe =
        getAggregatorsParam().getValues()[rand.nextInt(getAggregatorsParam().getValues().length)];
    String group = getGroupsParam().getValues()[rand.nextInt(getGroupsParam().getValues().length)];
    String prop =
        getPropertyParam().getValues()[rand.nextInt(getPropertyParam().getValues().length)];
    return StartpepHeadEntity.builder()
        .agregOpeClas(classAgreOpe)
        .agreOpe(agreOpe)
        .group(group)
        .prop(prop)
        .type(getType())
        .build();
  }
}
