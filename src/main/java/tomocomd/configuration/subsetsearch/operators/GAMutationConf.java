package tomocomd.configuration.subsetsearch.operators;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class GAMutationConf implements Serializable {
  GAMutationType gaMutationType;
  double prob;

  public GAMutationConf() {
    gaMutationType = GAMutationType.UNIFORM;
    prob = 0.1;
  }

  @Override
  public String toString() {
    return "{" + "gaMutationType=" + gaMutationType + ", prob=" + prob + '}';
  }
}
