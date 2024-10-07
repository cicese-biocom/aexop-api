package tomocomd.configuration.subsetsearch;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DCSEvolutiveConfigTest {

  DCSEvolutiveConfig dcsEvolutiveConfig;

  @Test
  void testToString() {
    dcsEvolutiveConfig = new DCSEvolutiveConfig();
    assertEquals(getString(), dcsEvolutiveConfig.toString());
  }

  String getString() {
    return "GA Algorithm search configuration{\n"
        + "\t\t\t\t Population size=100,\n"
        + "\t\t\t\t Incest=true,\n"
        + "\t\t\t\t Filters=[{type=NAN, options=[-t, DELETE]},{type=SE, options=[-t, 0.15]}],\n"
        + "\t\t\t\t Fitness function={type=Choquet integral, option=[-m, Q-measure, -i, Choquet, -mo, -l/0.5/-d/[0.3;0.3;0.1;0.05]]},\n"
        + "\t\t\t\t Selection={cant=20, type=Tournament, options=[-s, 5]},\n"
        + "\t\t\t\t Crossover={type=HUX, prob=1.0},\n"
        + "\t\t\t\t Mutation={gaMutationType=UNIFORM, prob=0.1},\n"
        + "\t\t\t\t Replace md={type=PARENT}\n"
        + "\t}";
  }
}
