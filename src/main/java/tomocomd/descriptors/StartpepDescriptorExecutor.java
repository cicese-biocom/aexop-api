package tomocomd.descriptors;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;
import org.apache.commons.math3.linear.RealMatrix;
import tomocomd.configuration.dcs.PDType;
import tomocomd.data.PopulationInstances;
import tomocomd.exceptions.AExOpDCSException;
import tomocomd.io.ReadPeptideFile;
import tomocomd.md.ComputeBatch;
import tomocomd.model.PeptideContainer;
import weka.core.Attribute;
import weka.core.DenseInstance;

public class StartpepDescriptorExecutor implements IPDComputer {
  @Override
  public PopulationInstances compute(Set<String> pdSet, String seqFilePath) {
    try {
      PeptideContainer peptides = ReadPeptideFile.readPeptideFile(seqFilePath);
      ComputeBatch computeBatch = new ComputeBatch(peptides.size(), pdSet.size());
      computeBatch.computeInBatch(peptides, new LinkedList<>(pdSet));
      RealMatrix real = computeBatch.getResults();

      ArrayList<Attribute> atts = new ArrayList<>();
      pdSet.forEach(head -> atts.add(new Attribute(head)));

      int numDesc = atts.size();
      PopulationInstances instances =
          new PopulationInstances(new File(seqFilePath).getName(), atts, numDesc);

      for (int i = 0; i < peptides.size(); i++) {
        double[] values = real.getRow(i);
        instances.add(new DenseInstance(1, values));
      }
      return instances;

    } catch (IOException e) {
      throw AExOpDCSException.ExceptionType.READING_SEQ_FILE_EXCEPTION_TYPE.get(e);
    }
  }

  @Override
  public PDType getType() {
    return PDType.STARTPEP;
  }
}
