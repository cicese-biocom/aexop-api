package tomocomd.utils;

import java.util.*;
import lombok.Getter;
import tomocomd.configuration.dcs.AHeadEntity;
import tomocomd.exceptions.AExOpDCSException;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.LinearRegression;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

public class Statistics {
  private Statistics() {
    throw new IllegalStateException();
  }

  public static double kurtosis(double[] att) throws AExOpDCSException {
    try {
      double m = Statistics.average(att);
      double s4 = Math.pow(Statistics.std(att), 4);
      double sum = 0;
      for (double v : att) {
        sum += (Math.pow(v - m, 4));
      }
      return sum / att.length * s4;
    } catch (Exception ex) {
      throw AExOpDCSException.ExceptionType.STATISTICS_EXCEPTION.get(
          "Error computing kurtosis function", ex);
    }
  }

  public static double average(double[] values) throws AExOpDCSException {

    try {
      double ave = 0;
      int cant = 0;
      for (double v : values) {
        if (!Double.isNaN(v)) {
          ave += v;
          cant++;
        }
      }
      return cant == 0 ? 0 : ave / cant;
    } catch (Exception ex) {
      throw AExOpDCSException.ExceptionType.STATISTICS_EXCEPTION.get(
          "Error computing average function", ex);
    }
  }

  public static double std(double[] values) throws AExOpDCSException {
    try {
      return Math.sqrt(variance(values));
    } catch (Exception ex) {
      throw AExOpDCSException.ExceptionType.STATISTICS_EXCEPTION.get(
          "Error computing std function", ex);
    }
  }

  public static double variance(double[] values) throws AExOpDCSException {
    try {
      double s = 0;
      double ave = average(values);

      for (double v : values) {
        s += Math.pow(v - ave, 2);
      }
      return s / values.length;
    } catch (Exception ex) {
      throw AExOpDCSException.ExceptionType.STATISTICS_EXCEPTION.get(
          "Error computing variance function", ex);
    }
  }

  public static double log2(double v) {
    return Math.log(v) / Math.log(2);
  }

  public static double se(double[] att) throws AExOpDCSException {
    Bin[] bins = new Bin[att.length];
    if (fillBins(att, bins)) {
      return calculateEntropy(bins);
    }
    return 0;
  }

  public static double median(double[] values) throws AExOpDCSException {
    try {
      double[] valuesCopy = Arrays.copyOf(values, values.length);
      Arrays.sort(valuesCopy);
      if (valuesCopy.length % 2 == 1) {
        return valuesCopy[valuesCopy.length / 2];
      }
      return (valuesCopy[valuesCopy.length / 2] + valuesCopy[valuesCopy.length / 2 - 1]) / 2;
    } catch (Exception ex) {
      throw AExOpDCSException.ExceptionType.STATISTICS_EXCEPTION.get(
          "Error computing median function", ex);
    }
  }

  private static double calculateEntropy(Bin[] bins) throws AExOpDCSException {
    try {
      double entropy = 0.;
      double prob;
      for (Bin bin : bins) {
        if (bin == null) {
          return -1d;
        }

        if (bin.getCount() > 0) {
          prob = (double) bin.getCount() / bins.length;
          entropy -= prob * log2(prob);
        }
      }
      return entropy;
    } catch (Exception ex) {
      throw AExOpDCSException.ExceptionType.STATISTICS_EXCEPTION.get(
          "Error computing Shannon entropy function", ex);
    }
  }

  public static int hammingDistance(AHeadEntity head1, AHeadEntity head2) throws AExOpDCSException {
    if (head1.getType() != head2.getType()) {
      throw AExOpDCSException.ExceptionType.STATISTICS_EXCEPTION.get(
          String.format(
              "Error computing hamming distance function, the heads have to be the same type, %s and %s are not equals",
              head1.getType(), head2.getType()));
    }

    try {
      // get de params and values from heads
      String value1;
      String value2;
      Map<String, String> pA1 = head1.parseHead2Map();
      Map<String, String> pA2 = head2.parseHead2Map();

      // get de head with max number of params
      Set<String> keys = new HashSet<>(pA1.keySet());
      keys.addAll(pA2.keySet());

      int d = 0;

      for (String key : keys) {
        value1 = pA1.getOrDefault(key, "");
        value2 = pA2.getOrDefault(key, "");
        if (!value1.equals(value2)) {
          d++;
        }
      }
      return d;
    } catch (Exception ex) {
      throw AExOpDCSException.ExceptionType.STATISTICS_EXCEPTION.get(
          "Error computing hamming distance function", ex);
    }
  }

  public static double r2(double[] x, double[] y) throws AExOpDCSException {
    LinearRegression regresion;
    int numInst = x.length;
    int j;
    Instances train =
        new Instances(
            "train", new ArrayList<>(Arrays.asList(new Attribute("x"), new Attribute("y"))), 2);
    for (j = 0; j < numInst; j++) {
      Instance inst = new DenseInstance(1.0, new double[] {x[j], y[j]});
      train.add(inst);
    }

    try {
      regresion = new LinearRegression();
      train.setClassIndex(1);
      regresion.buildClassifier(train);
      Evaluation eva = new Evaluation(train);
      eva.crossValidateModel(regresion, train, 10, new Random(1));
      return Math.abs(eva.correlationCoefficient());
    } catch (Exception ex) {
      throw AExOpDCSException.ExceptionType.STATISTICS_EXCEPTION.get(ex);
    }
  }

  private static boolean fillBins(double[] desc, Bin[] bins) {
    double min = Double.MAX_VALUE;
    double max = Double.MIN_VALUE;
    for (double auxVal : desc) {

      if (!Double.isFinite(auxVal)) {
        auxVal = 0;
      }

      if (auxVal < min) {
        min = auxVal;
      }

      if (auxVal > max) {
        max = auxVal;
      }
    }

    Bin bin;
    double binWidth;
    double lower;
    double upper;
    int binIndex;
    binWidth = (max - min) / bins.length;
    lower = min;

    for (int i = 0; i < bins.length; i++) {
      if (i == bins.length - 1) {
        bin = new Bin(lower, max);
      } else {
        upper = min + (i + 1) * binWidth;
        bin = new Bin(lower, upper);
        lower = upper;
      }
      bins[i] = bin;
    }

    for (double val : desc) {
      binIndex = bins.length - 1;

      if (!Double.isFinite(val)) {
        val = 0;
      }

      if (val < max) {
        double fraction = (val - min) / (max - min);
        if (fraction < 0.0) {
          fraction = 0.0;
        }
        binIndex = (int) (fraction * bins.length);
        // rounding could result in binIndex being equal to binsXY
        // which will cause an IndexOutOfBoundsException - see bug
        // report 1553088
        if (binIndex >= bins.length) {
          binIndex = bins.length - 1;
        }
      }
      bin = bins[binIndex];
      bin.incrementCount();
    }

    return true;
  }

  @Getter
  static class Bin {

    /** The number of items in the bin. */
    private int count;

    /** The start boundary. */
    private double startBoundary;

    /** The end boundary. */
    private double endBoundary;

    /**
     * Creates a new bin.
     *
     * @param startBoundary the start boundary.
     * @param endBoundary the end boundary.
     */
    Bin(double startBoundary, double endBoundary) {
      if (startBoundary > endBoundary) {
        throw new IllegalArgumentException("Bin:  startBoundary > endBoundary.");
      }
      this.count = 0;
      this.startBoundary = startBoundary;
      this.endBoundary = endBoundary;
    }

    /** Increments the item count. */
    public void incrementCount() {
      this.count++;
    }

    /**
     * Returns the bin width.
     *
     * @return The bin width.
     */
    public double getBinWidth() {
      return this.endBoundary - this.startBoundary;
    }
  }
}
