package tomocomd.configuration.dcs.startpep;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import tomocomd.configuration.dcs.AParamsDCS;

public class ClassicalAggParam extends AParamsDCS {

  public ClassicalAggParam() {
    super();
  }

  public ClassicalAggParam(ClassicalAggParam paramsHead) {
    super(paramsHead);
  }

  @Override
  public void setDefault() {
    //    params.put("IB", true);
    params.put("AC", true);
    params.put("GV", true);
    params.put("TS", true);
    params.put("ES", true);
    params.put("MIC", true);
    params.put("", true);
  }

  @Override
  public String[] getValues() {
    List<String> values = new LinkedList<>();
    if (Boolean.TRUE.equals(params.get("MIC"))) values.add("MIC");
    //        if (Boolean.TRUE.equals(params.get("IB"))) values.add("IB");
    if (Boolean.TRUE.equals(params.get("ES"))) values.add("ES");
    if (Boolean.TRUE.equals(params.get(""))) values.add("");
    if (Boolean.TRUE.equals(params.get("AC")))
      values.addAll(List.of("AC[1]", "AC[2]", "AC[3]", "AC[4]", "AC[5]", "AC[6]", "AC[7]"));
    if (Boolean.TRUE.equals(params.get("GV")))
      values.addAll(List.of("GV[1]", "GV[2]", "GV[3]", "GV[4]", "GV[5]", "GV[6]", "GV[7]"));
    if (Boolean.TRUE.equals(params.get("TS")))
      values.addAll(List.of("TS[1]", "TS[2]", "TS[3]", "TS[4]", "TS[5]", "TS[6]", "TS[7]"));
    return values.toArray(new String[0]);
  }

  @Override
  public String getParamName() {
    return "Classical aggregators";
  }

  @Override
  public String toString() {
    return "Classical Aggregation Operator="
        + Arrays.stream(getValues()).collect(Collectors.joining(",", "[", "]"));
  }
}
