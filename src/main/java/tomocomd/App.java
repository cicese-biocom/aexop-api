package tomocomd;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import tomocomd.configuration.subsetsearch.AexopConfig;
import tomocomd.subsetsearch.AexopDcs;

/** Hello world! */
public class App {
  public static void main(String[] args) throws Exception {

    AexopConfig conf = new AexopConfig();
    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(new File("project_seq.json"), conf);

    String pathFasta = new File("data\\TR_starPep_AF_training.fasta").getAbsolutePath();
    String output = new File("data\\salida\\aexop_dcs_output.csv").getAbsolutePath();
    String target = new File("data\\TR_starPep_AF_training_class.csv").getAbsolutePath();
    AexopDcs algorithm = new AexopDcs(conf, output, pathFasta, target);
    algorithm.compute();
  }
}
