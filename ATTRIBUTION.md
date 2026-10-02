# Authorship and source provenance

This repository collects Lukaš Patrik Magalinski's 2025 parallel-computing coursework from the Informatics programme at Vilnius University, Faculty of Mathematics and Informatics.

The repository contains four labs, a historical MPI source variant, and supporting documentation and illustrations. The primary source files map to the saved coursework folders as follows.

## Source mapping

| Coursework location | Repository location |
| --- | --- |
| `lyg_sk_1/src/Main.java` | `labs/01-warehouse-race/Main.java` |
| `lygt_sk_2_good/src/ColorDemo.java` | `labs/02-color-monitor/ColorDemo.java` |
| `lygt_sk_2_good/src/ColorSynchronizer.java` | `labs/02-color-monitor/ColorSynchronizer.java` |
| `lyg_sk3/src/Mandelbrot.java` | `labs/03-mandelbrot-java/Mandelbrot.java` |
| `lyg_sk3/src/TTestMandelbrot.java` | `labs/03-mandelbrot-java/TTestMandelbrot.java` |
| `lyg_sk4/mandelbrot_mpi.c` | `labs/04-mandelbrot-mpi/mandelbrot_mpi.c` |

The archived `archive/mpi/mandelbrot-serial-variant.c` maps to `lyg_sk4/malderbrot.txt`. See the [archive notes](archive/README.md) for its relationship to the primary MPI lab.

## Course material and acknowledgements

The original lab 3 folder also contains the generic `TTest.java` benchmark, whose header identifies **R. Vaicekauskas** as its author. That lecturer-authored file is not included here. `TTestMandelbrot.java` follows the course benchmark structure, with a Mandelbrot workload. Credit for that teaching pattern belongs to R. Vaicekauskas; this repository does not claim independent authorship of the generic benchmark design.

The lab 2 assignment is the white/red group synchronization problem, described in the coursework as a variation of the readers/writers problem. The assignment text and commented alternative source drafts are not republished as separate programs.

The saved diagrams and measurements belong to the historical coursework record. They must be read with their source captions and parameter descriptions, rather than treated as new measurements of this repository.

## Additional material

The [Slurm submission template](hpc/submit.slurm) is a repository helper based on the compiler/launcher workflow in the original coursework scripts. It uses eight ranks and leaves account, partition and module selection to the cluster user.

Generic MPI teaching examples, compiled artifacts, IDE state and cluster access notes are not part of the source collection.
