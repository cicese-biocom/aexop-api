package tomocomd.configuration.dcs.startpep;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tomocomd.configuration.dcs.AHeadEntity;
import tomocomd.configuration.dcs.PDType;
import tomocomd.exceptions.AExOpDCSException;
import tomocomd.md.HeaderValidator;
import tomocomd.utils.Constants;

class StartpepDCSTest {

  StartpepDCS startpepDCS;

  @BeforeEach
  void setUp() {
    startpepDCS = new StartpepDCS();
  }

  @Test
  void getType() {
    assertEquals(PDType.STARTPEP, startpepDCS.getType());
  }

  @Test
  void randomHeading() {
    AHeadEntity aHeadEntity = startpepDCS.randomHeading();
    assertNotNull(aHeadEntity);
    HeaderValidator.validateHeader(aHeadEntity.toString());
  }

  @Test
  void getSetDim() {
    assertEquals(29920, startpepDCS.getSetDim());
  }

  @Test
  void getDesc() {
    assertEquals(
        "PD(29920 molecular descriptors){\n"
            + "\t\t Type=STARTPEP,\n"
            + "\t\t Classical Aggregation Operator=[],\n"
            + "\t\t Aggregation Operator={\n"
            + "\t\t Norms=[N1,N2,N3],\n"
            + "\t\t Means=[GM,AM,P2,P3,HM],\n"
            + "\t\t statistics=[V,S,K,SD,VC,RA,Q1,Q2,Q3,I50,MX,MN],\n"
            + "\t\t Choquet=[CHOQUET[A;-0.75;AO2;0.6],CHOQUET[A;-0.75;AO1;0.3],CHOQUET[A;-0.25;AO2;0.6],CHOQUET[A;0.75;AO2;0.6],CHOQUET[A;0.75;AO1;0.2],CHOQUET[A;0.25;AO1;0.9],CHOQUET[A;0.25;AO2;0.6],CHOQUET[A;0.5;AO1;0.2],CHOQUET[A;0.75;AO2;0.5],CHOQUET[A;0.75;AO1;0.8],CHOQUET[A;0.5;AO1;0.9],CHOQUET[A;0.25;AO1;0.8],CHOQUET[A;-0.25;AO2;1.0],CHOQUET[A;-0.25;AO2;0.8],CHOQUET[A;0.5;AO2;0.6],CHOQUET[A;-0.25;AO1;0.8],CHOQUET[A;-0.75;AO1;0.9],CHOQUET[A;0.5;AO2;0.9],CHOQUET[A;0.75;AO1;0.9],CHOQUET[A;-0.75;AO2;0.9],CHOQUET[A;-0.75;AO1;1.0],CHOQUET[A;0.5;AO2;0.8],CHOQUET[A;0.25;AO2;0.5],CHOQUET[A;-0.75;AO2;0.7],CHOQUET[A;-0.75;AO2;1.0],CHOQUET[A;-0.5;AO1;0.3],CHOQUET[A;-0.75;AO2;0.0],CHOQUET[A;-0.75;AO1;0.2],CHOQUET[A;-0.5;AO2;0.0],CHOQUET[A;0.5;AO1;0.8],CHOQUET[A;0.25;AO1;0.2],CHOQUET[A;-0.5;AO1;0.2],CHOQUET[A;0.5;AO2;0.5],CHOQUET[D;-0.75;AO2;0.6],CHOQUET[D;-0.75;AO1;0.3],CHOQUET[D;-0.25;AO2;0.6],CHOQUET[D;0.75;AO2;0.6],CHOQUET[D;0.75;AO1;0.2],CHOQUET[D;0.25;AO1;0.9],CHOQUET[D;0.25;AO2;0.6],CHOQUET[D;0.5;AO1;0.2],CHOQUET[D;0.75;AO2;0.5],CHOQUET[D;0.75;AO1;0.8],CHOQUET[D;0.5;AO1;0.9],CHOQUET[D;0.25;AO1;0.8],CHOQUET[D;-0.25;AO2;1.0],CHOQUET[D;-0.25;AO2;0.8],CHOQUET[D;0.5;AO2;0.6],CHOQUET[D;-0.25;AO1;0.8],CHOQUET[D;-0.75;AO1;0.9],CHOQUET[D;0.5;AO2;0.9],CHOQUET[D;0.75;AO1;0.9],CHOQUET[D;-0.75;AO2;0.9],CHOQUET[D;-0.75;AO1;1.0],CHOQUET[D;0.5;AO2;0.8],CHOQUET[D;0.25;AO2;0.5],CHOQUET[D;-0.75;AO2;0.7],CHOQUET[D;-0.75;AO2;1.0],CHOQUET[D;-0.5;AO1;0.3],CHOQUET[D;-0.75;AO2;0.0],CHOQUET[D;-0.75;AO1;0.2],CHOQUET[D;-0.5;AO2;0.0],CHOQUET[D;0.5;AO1;0.8],CHOQUET[D;0.25;AO1;0.2],CHOQUET[D;-0.5;AO1;0.2],CHOQUET[D;0.5;AO2;0.5]],\n"
            + "\t\t Gowawa=[v[0.9;1;AO2-OWA;1.0;0.0;1;ES2-OWA;0.9;0.0],GOWAWA[1.0;1;AO2-OWA;1.0;0.0;1;NONE;0.0;0.0],GOWAWA[0.1;1;S-OWA;1.0;0.0;1;ES2-OWA;0.9;0.0],GOWAWA[0.2;1;S-OWA;1.0;0.0;1;ES2-OWA;0.9;0.0],GOWAWA[0.7;1;S-OWA;0.8;0.2;1;ES2-OWA;0.9;0.0],GOWAWA[0.8;1;S-OWA;1.0;0.0;1;ES2-OWA;0.9;0.0],GOWAWA[0.5;1;ES1-OWA;0.7;0.0;1;ES2-OWA;0.9;0.0],GOWAWA[0.2;1;AO2-OWA;1.0;0.0;1;ES2-OWA;0.9;0.0],GOWAWA[1.0;1;ES1-OWA;0.7;0.0;1;NONE;0.0;0.0],GOWAWA[0.5;1;S-OWA;1.0;0.0;1;ES2-OWA;0.9;0.0],GOWAWA[0.9;1;ES1-OWA;0.7;0.0;1;ES2-OWA;0.9;0.0],GOWAWA[0.2;1;ES1-OWA;0.7;0.0;1;ES2-OWA;0.9;0.0],GOWAWA[0.7;1;ES1-OWA;0.7;0.0;1;ES2-OWA;0.9;0.0],GOWAWA[0.6;1;AO2-OWA;1.0;0.0;1;ES2-OWA;0.9;0.0],GOWAWA[0.5;1;AO2-OWA;1.0;0.0;1;ES2-OWA;0.9;0.0],GOWAWA[1.0;1;S-OWA;0.8;0.2;1;NONE;0.0;0.0],GOWAWA[0.7;1;AO2-OWA;1.0;0.0;1;ES2-OWA;0.9;0.0],GOWAWA[0.0;1;NONE;0.0;0.0;1;S-OWA;1.0;0.0],GOWAWA[0.1;0;W-OWA;0.1;0.6;2;W-OWA;0.1;0.2],GOWAWA[0.0;1;NONE;0.0;0.0;2;W-OWA;0.4;0.5],GOWAWA[0.0;1;NONE;0.0;0.0;2;W-OWA;0.7;0.8],GOWAWA[0.1;2;ES2-OWA;0.9;0.0;2;W-OWA;0.3;0.4],GOWAWA[0.3;1;S-OWA;1.0;0.0;1;ES2-OWA;0.9;0.0],GOWAWA[0.4;1;ES1-OWA;0.7;0.0;1;ES2-OWA;0.9;0.0],GOWAWA[0.9;1;S-OWA;1.0;0.0;1;ES2-OWA;0.9;0.0],GOWAWA[0.0;1;NONE;0.0;0.0;2;W-OWA;0.5;0.6],GOWAWA[0.1;0;AO1-OWA;1.0;0.0;2;S-OWA;0.8;0.1],GOWAWA[0.0;1;NONE;0.0;0.0;2;W-OWA;0.5;0.7],GOWAWA[0.6;1;S-OWA;1.0;0.0;2;W-OWA;0.9;1.0],GOWAWA[0.1;2;ES2-OWA;0.9;0.0;2;W-OWA;0.2;0.3],GOWAWA[1.0;1;ES1-OWA;0.7;0.0;1;NONE;0.0;0.0],GOWAWA[1.0;1;S-OWA;1.0;0.0;1;NONE;0.0;0.0],GOWAWA[1.0;2;ES2-OWA;0.9;0.0;1;NONE;0.0;0.0],GOWAWA[0.5;1;AO2-OWA;1.0;0.0;1;ES2-OWA;0.9;0.0],GOWAWA[1.0;1;S-OWA;0.8;0.2;1;NONE;0.0;0.0],GOWAWA[0.0;1;NONE;0.0;0.0;0;S-OWA;0.0;1.0],GOWAWA[0.7;2;ES2-OWA;0.9;0.0;0;S-OWA;0.0;1.0],GOWAWA[0.1;2;ES2-OWA;0.9;0.0;2;W-OWA;0.7;0.8],GOWAWA[0.1;2;ES2-OWA;0.9;0.0;0;W-OWA;0.0;0.1],GOWAWA[0.9;1;AO2-OWA;1.0;0.0;1;ES2-OWA;0.9;0.0],GOWAWA[0.2;2;ES2-OWA;0.9;0.0;2;W-OWA;0.4;0.6],GOWAWA[0.3;2;S-OWA;0.6;0.0;2;W-OWA;0.9;1.0],GOWAWA[0.4;1;AO2-OWA;1.0;0.0;1;ES2-OWA;0.9;0.0],GOWAWA[1.0;1;AO2-OWA;1.0;0.0;1;NONE;0.0;0.0],GOWAWA[0.0;1;NONE;0.0;0.0;1;S-OWA;1.0;0.0],GOWAWA[0.8;1;AO2-OWA;1.0;0.0;1;ES2-OWA;0.9;0.0],GOWAWA[0.9;1;ES1-OWA;0.7;0.0;1;ES2-OWA;0.9;0.0],GOWAWA[0.2;2;S-OWA;0.6;0.0;2;S-OWA;0.8;0.1]],\n"
            + "\t\t Information=[TIC,SIC]\n"
            + "\t\t},\n"
            + "\t\t Chemical Groups=[T,A,H,P,R,B,F,N,C,U,D],\n"
            + "\t\t Aminoacid properties=[ptt,gcp1,gcp2,eps,scm,scv,pie,pah,pbs,isa,z1,z2,z3,mw,bi,khh,hwhh,cch,kh,cdch]\n"
            + "\t\t}",
        startpepDCS.getDesc());
  }

  @Test
  void getValues4ParamClass() {
    assertEquals(1, startpepDCS.getValues4Param(Constants.CLASAGGOPECONST).length);
  }

  @Test
  void getValues4ParamAgg() {
    assertEquals(136, startpepDCS.getValues4Param(Constants.AGGOPECONST).length);
  }

  @Test
  void getValues4ParamGroups() {
    assertEquals(11, startpepDCS.getValues4Param(Constants.GROUPSCONST).length);
  }

  @Test
  void getValues4ParamProp() {
    assertEquals(20, startpepDCS.getValues4Param(Constants.PROPCONST).length);
  }

  @Test
  void getValues4ParamException() {
    assertThrows(AExOpDCSException.class, () -> startpepDCS.getValues4Param("Constants.PROPCONST"));
  }
}
