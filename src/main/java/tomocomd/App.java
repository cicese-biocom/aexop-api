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

    String pathFasta = new File("data\\TR_starPep_AP.fasta").getAbsolutePath();
    String output = new File("data\\salida\\aexop_dcs_output_APP.csv").getAbsolutePath();
    String target = new File("data\\TR_starPep_AP_class.csv").getAbsolutePath();
    AexopDcs algorithm = new AexopDcs(conf, output, pathFasta, target);
    algorithm.compute();
  }
}
