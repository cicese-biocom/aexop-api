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


```mermaid
graph TD;
    A[Inicio] --> B[Inicialización de Variables];
    B --> C[Inicialización de Múltiples Poblaciones];
    C -->|Cada población i| C1[Inicializar Población i];
    C1 --> D{¿t < T?};

    D -- Sí --> E[Evaluación de Cada Población];
    E -->|Cada población i| E1[Calcular descriptores de Población i];
    E1 --> G1[Evaluar fitness de Población i];
    G1 --> H1[Selección de Padres en Población i];
    H1 --> I1[Crossover HUX en Población i];
    I1 --> J1[Mutación en Población i];
    J1 --> K1[Reemplazo de Población i];

    K1 --> L[Unión y Ordenación de Soluciones];
    L --> M[Calcular la Mejor Solución];
    M --> N{¿Mejor Fitness Encontrado?};
    
    N -- Sí --> O[Actualizar Mejor Solución];
    N -- No --> P{¿Reiniciar Poblaciones?};
    
    P -- Sí --> Q[Reinicialización de Poblaciones];
    Q -->|Cada población i| Q1[Reinicializar Población i];
    Q1 --> R[Incrementar t];
    P -- No --> R;
    
    R --> D;
    D -- No --> S[Retornar Mejor Subset];



