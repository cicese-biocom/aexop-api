package tomocomd;

import java.io.File;
import tomocomd.configuration.subsetsearch.AexopConfig;
import tomocomd.subsetsearch.AexopDcs;

/** Hello world! */
public class App {
  public static void main(String[] args) {

    AexopConfig conf = new AexopConfig();

    conf.getDcsEvolutiveConfig().setNumDesc(50);

    File folder = new File("E:\\Research\\Colaboracion\\");
    String pathFasta = new File(folder, "EX_starPep_AP.fasta").getAbsolutePath();
    String output = new File(folder, "aexop_dcs_output.csv").getAbsolutePath();
    String target = new File(folder, "EX_starPep_AP_target.csv").getAbsolutePath();
    AexopDcs algorithm = new AexopDcs(conf, output, pathFasta, target);
    algorithm.compute();
  }
}
