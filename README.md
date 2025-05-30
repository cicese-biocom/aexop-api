# aexop-api


![Badge en Desarollo](https://img.shields.io/badge/STATUS-IN%20PROGRESS-green)
![Java 11](https://img.shields.io/badge/Java-11-blue.svg)
![Maven](https://img.shields.io/badge/Maven-3.8.8-blue.svg)
[![API-Build And Release](https://github.com/cicese-biocom/aexop-api/actions/workflows/maven_release.yml/badge.svg)](https://github.com/cicese-biocom/aexop-api/actions/workflows/maven_release.yml)
## Description

A Java API to execute AExOp-DCS algorithm for macromolecules. AExOp-DCS is a new genetic algorithm-based strategy to
automatically determine an “optimal” initial set of MDs. AExOp-DCS is rather different from
the traditional strategy since it does not overproduce and then select MDs; it determines through
an evolutionary multi-criteria optimization the best MDs according to the compounds and endpoint(s)
under study. In other words, AExOp-DCS is a dataset- and endpoint-guided algorithm.

**For more information, please read:**  
[L. A. García-González, Y. Marrero-Ponce, C. A. Brizuela, C. R. García-Jacas, *Molecular Informatics* **2023**, 42, 2200227](https://doi.org/10.1002/minf.202200227).

## Graphical abstract
![src/main/resources/img.png](https://github.com/cicese-biocom/aexop-dcs/raw/main/src/main/resources/img.png)
