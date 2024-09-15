/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tomocomd.subsetsearch.filters;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import tomocomd.configuration.filters.FilterConfig;
import tomocomd.exceptions.AExOpDCSException;

/**
 * @author Potter
 */
public class FilterFactory {

  private FilterFactory() {
    throw new IllegalStateException();
  }

  public static AbstractMDFilter getFilters(List<FilterConfig> confs) throws AExOpDCSException {
    AbstractMDFilter f = new FilterBase();

    confs =
        confs.stream()
            .sorted(Comparator.comparing(FilterConfig::getLevel))
            .collect(Collectors.toList());

    for (FilterConfig conf : confs) {
      switch (conf.getType()) {
        case NAN:
          f = new NaNFilter(f);
          break;
        case CORR:
          f = new CorrelationFilter(f);
          break;
        case KUR:
          f = new KurtosisFilter(f);
          break;
        case SE:
          f = new SEFilter(f);
          break;
        case R2:
          f = new R2Filter(f);
          break;
      }
      f.setOptions(conf.getOptions());
    }
    return f;
  }
}
