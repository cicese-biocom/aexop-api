package tomocomd.configuration.dcs.startpep;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.ArrayUtils;
import tomocomd.configuration.dcs.AParamsDCS;
import tomocomd.configuration.dcs.startpep.aggregtions.*;
import tomocomd.exceptions.AExOpDCSException;

public class AggregatorsParam extends AParamsDCS {

  private NormsAggParam normsAggParam;
  private MeansAggParam meansAggParam;
  private StatisticsAggParam statisticsAggParam;
  private ChoquetAggParam choquetAggParam;
  private GowawaAggParam gowawaAggParam;
  private InformationAggParam informationAggParam;

  public AggregatorsParam() {
    super();
  }

  @Override
  public void init() {
    normsAggParam = new NormsAggParam();
    meansAggParam = new MeansAggParam();
    statisticsAggParam = new StatisticsAggParam();
    choquetAggParam = new ChoquetAggParam();
    gowawaAggParam = new GowawaAggParam();
    informationAggParam = new InformationAggParam();
  }

  public AggregatorsParam(AggregatorsParam paramsHead) {
    this.normsAggParam = new NormsAggParam(paramsHead.getNormsAggParam());
    this.meansAggParam = new MeansAggParam(paramsHead.getMeansAggParam());
    this.statisticsAggParam = new StatisticsAggParam(paramsHead.getStatisticsAggParam());
    this.choquetAggParam = new ChoquetAggParam(paramsHead.getChoquetAggParam());
    this.gowawaAggParam = new GowawaAggParam(paramsHead.getGowawaAggParam());
    this.informationAggParam = new InformationAggParam(paramsHead.getInformationAggParam());
    validate();
  }

  @Override
  public void setDefault() {
    Stream.of(
            normsAggParam,
            meansAggParam,
            statisticsAggParam,
            choquetAggParam,
            gowawaAggParam,
            informationAggParam)
        .forEach(AParamsDCS::setDefault);
  }

  @Override
  public Map<String, Boolean> getParams() {
    return Stream.of(
            normsAggParam,
            meansAggParam,
            statisticsAggParam,
            choquetAggParam,
            gowawaAggParam,
            informationAggParam)
        .map(AParamsDCS::getParams)
        .flatMap(map -> map.entrySet().stream())
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  @Override
  public String[] getValues() {
    return ArrayUtils.addAll(
        Stream.of(
                normsAggParam,
                meansAggParam,
                statisticsAggParam,
                choquetAggParam,
                gowawaAggParam,
                informationAggParam)
            .map(AParamsDCS::getValues)
            .flatMap(Stream::of)
            .toArray(String[]::new));
  }

  @Override
  public void validate() {
    if (getValues().length == 0) statisticsAggParam.setDefault();
  }

  @Override
  public void setValue(String key, Boolean value) throws AExOpDCSException {
    Optional<AParamsDCS> param =
        Stream.of(
                normsAggParam,
                meansAggParam,
                statisticsAggParam,
                choquetAggParam,
                gowawaAggParam,
                informationAggParam)
            .filter(paramsHead -> paramsHead.getParams().containsKey(key))
            .findFirst();
    if (param.isPresent()) param.get().setValue(key, value);
    else
      throw AExOpDCSException.ExceptionType.MD_PARAM_EXCEPTION_TYPE.get(
          String.format("Invalid param name %s for param %s", key, getParamName()));
  }

  @Override
  public Boolean getValue(String key) throws AExOpDCSException {
    Optional<AParamsDCS> param =
        Stream.of(
                normsAggParam,
                meansAggParam,
                statisticsAggParam,
                choquetAggParam,
                gowawaAggParam,
                informationAggParam)
            .filter(paramsHead -> paramsHead.getParams().containsKey(key))
            .findFirst();
    if (param.isPresent()) return param.get().getValue(key);
    else
      throw AExOpDCSException.ExceptionType.MD_PARAM_EXCEPTION_TYPE.get(
          String.format("Invalid param name %s for param %s", key, getParamName()));
  }

  @Override
  public String getParamName() {
    return "Aggregations Param";
  }

  @Override
  public String toString() {
    return "Aggregation Operator={"
        + "\n\t\t Norms="
        + Arrays.stream(getNormsAggParam().getValues()).collect(Collectors.joining(",", "[", "]"))
        + ",\n\t\t Means="
        + Arrays.stream(getMeansAggParam().getValues()).collect(Collectors.joining(",", "[", "]"))
        + ",\n\t\t statistics="
        + Arrays.stream(getStatisticsAggParam().getValues())
            .collect(Collectors.joining(",", "[", "]"))
        + ",\n\t\t Choquet="
        + Arrays.stream(getChoquetAggParam().getValues()).collect(Collectors.joining(",", "[", "]"))
        + ",\n\t\t Gowawa="
        + Arrays.stream(getGowawaAggParam().getValues()).collect(Collectors.joining(",", "[", "]"))
        + ",\n\t\t Information="
        + Arrays.stream(getInformationAggParam().getValues())
            .collect(Collectors.joining(",", "[", "]"))
        + "\n\t\t}";
  }

  public NormsAggParam getNormsAggParam() {
    return normsAggParam;
  }

  public MeansAggParam getMeansAggParam() {
    return meansAggParam;
  }

  public StatisticsAggParam getStatisticsAggParam() {
    return statisticsAggParam;
  }

  public ChoquetAggParam getChoquetAggParam() {
    return choquetAggParam;
  }

  public GowawaAggParam getGowawaAggParam() {
    return gowawaAggParam;
  }

  public InformationAggParam getInformationAggParam() {
    return informationAggParam;
  }
}
