package tomocomd.subsetsearch.filters;

import java.util.Arrays;
import tomocomd.data.PopulationInstances;
import tomocomd.exceptions.AExOpDCSException;

/**
 * @author Potter
 */
public abstract class AbstractFilterDecorator extends AbstractMDFilter {

  AbstractMDFilter filter;

  protected AbstractFilterDecorator(AbstractMDFilter filter) {
    super();
    this.filter = filter;
  }

  @Override
  public void filtering(PopulationInstances data) throws AExOpDCSException {
    filter.filtering(data);

    try {
      int[] indexs = getRemovePosition(data);
      Arrays.sort(indexs);

      for (int i = indexs.length - 1; i >= 0; i--)
        if (indexs[i] != data.classIndex()) data.deleteAttributeAt(indexs[i]);
    } catch (Exception ex) {
      throw AExOpDCSException.ExceptionType.FILTER_EXCEPTION.get(
          String.format("Error applying %s filter", this.getClass().getSimpleName()), ex);
    }
  }

  protected abstract int[] getRemovePosition(PopulationInstances data) throws AExOpDCSException;
}
