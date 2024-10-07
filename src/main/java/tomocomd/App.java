package tomocomd;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import tomocomd.configuration.subsetsearch.AexopConfig;

/** Hello world! */
public class App {
  public static void main(String[] args) throws Exception {

    AexopConfig conf = new AexopConfig();

    try (ObjectOutputStream datafile =
        new ObjectOutputStream(
            new BufferedOutputStream(Files.newOutputStream(new File("project.dcsseq").toPath())))) {
      datafile.writeObject(conf);
    }

    //    conf.getDcsEvolutiveConfig().setNumDesc(50);
    //
    //    File folder = new File("E:\\Research\\Colaboracion\\");
    //    String pathFasta = new File(folder, "EX_starPep_AP.fasta").getAbsolutePath();
    //    String output = new File(folder, "aexop_dcs_output.csv").getAbsolutePath();
    //    String target = new File(folder, "EX_starPep_AP_target.csv").getAbsolutePath();
    //    AexopDcs algorithm = new AexopDcs(conf, output, pathFasta, target);
    //    algorithm.compute();
  }
}
