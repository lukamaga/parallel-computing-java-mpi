# Historical benchmark results

These are recorded **2025 coursework results**, not fresh benchmark measurements. Times are in seconds; reported speedup is the dimensionless ratio `T(1) / T(p)`. Original numeric precision is preserved, including the report's rounding.

## Reading the measurements

- **Java:** the report identifies a MacBook Pro with Apple M3 Max, 16 CPU cores (12 performance and 4 efficiency) and 64 GB RAM. The implementation uses CPU threads; the GPU listed in the report's machine description is not part of this computation.
- **MPI:** the report describes Slurm's `main` partition and an Open MPI module. It does not record CPU model, node placement, software versions or measurement repetition count. Java and MPI times therefore do not support a direct language or machine comparison.
- **MPI baseline:** the main `mandelbrot_mpi.c` gives rank 0 a coordinator-only role and needs at least two ranks. The [archived serial variant](../../archive/mpi/mandelbrot-serial-variant.c) includes a one-rank rendering path. This provides a possible source for a nonzero serial baseline, but the report does not identify its exact code revision. The variant also does not resolve the provenance of every excess-rank measurement. The speedups below remain historical report ratios.
- **Java table versus charts:** the report contains two different numeric series. Its table starts at 86.927 s, whereas the chart labels start at 86.507 s. The archive does not explain the difference; they are kept separate below. The archived original figures retain those labels.

The complete data and figure provenance are also available in [historical-results.json](historical-results.json). See the [figure gallery](gallery.md) for all preserved illustrations.

## Java: report table

Source: [Java Mandelbrot report](../reports/java-mandelbrot-report.pdf), page 2 (`lyg_sk3/Ataskaita.pdf` in the archive). Image: **1920 × 1080** (2,073,600 pixels); `maxIter=100000`; `grainSize=32`. The report command is `java TTestMandelbrot 1920 1080 100000 32`.

| Threads | Time (s), report table | Reported speedup |
| ---: | ---: | ---: |
| 1 | 86.927 | 1.000000 |
| 2 | 44.641 | 1.947246 |
| 4 | 25.039 | 3.471664 |
| 8 | 12.631 | 6.882036 |
| 16 | 9.772 | 8.895518 |
| 32 | 9.797 | 8.872818 |

The fastest recorded row is 16 threads at 9.772 s (8.895518× reported speedup). The 32-thread row is slightly slower. This is an observation from the recorded workload, not a universal thread-count recommendation.

### Java: separate chart-label series

Source: labels in the original runtime, speedup and efficiency images, also embedded on report pages 2-3. These values do **not** replace the table above. Percentages and speedups are copied from the labels, not recomputed from rounded timings.

| Threads | Chart time label (s) | Chart speedup label | Chart efficiency label |
| ---: | ---: | ---: | ---: |
| 1 | 86.507 | 1.000 | 100.00% |
| 2 | 44.509 | 1.944 | 97.20% |
| 4 | 25.041 | 3.455 | 86.38% |
| 8 | 12.638 | 6.845 | 85.56% |
| 16 | 9.812 | 8.816 | 55.10% |
| 32 | 9.922 | 8.719 | 27.25% |

The iteration-limit and grain-size figures contain additional historical sweeps. Numeric source tables for these sweeps are unavailable; the original figures are retained. The report explicitly gives `grainSize=32` for the iteration-limit sweep and `maxIter=10000` for the grain-size sweep, but does not restate their image dimensions.

## MPI: report and timing log

Sources: [MPI Mandelbrot report](../reports/mpi-mandelbrot-report.pdf), pages 1-2 (`lyg_sk4/Ataskaita_2.pdf` in the archive), and the preserved [original timing log](mpi-10000-iterations-original.txt). All three series use `maxIter=10000` and `grainSize=32`. Process counts include the rank-0 coordinator. See the baseline caveat above before interpreting speedup.

### 720 × 720

| MPI processes | Time (s) | Reported speedup |
| ---: | ---: | ---: |
| 1 | 5.596834 | 1 |
| 2 | 5.850618 | 0.9566227020803615 |
| 4 | 1.926630 | 2.904986427077332 |
| 8 | 0.909775 | 6.151888104201588 |
| 16 | 0.869972 | 6.433349579066913 |
| 32 | 0.992450 | 5.639411557257293 |
| 64 | 0.974330 | 5.744289922305584 |
| 128 | 0.994637 | 5.627011663551627 |
| 256 | 0.994695 | 5.626683556265991 |

### 1920 × 1080

| MPI processes | Time (s) | Reported speedup |
| ---: | ---: | ---: |
| 1 | 22.331962 | 1 |
| 2 | 23.275706 | 0.9594536896109618 |
| 4 | 7.761372 | 2.877321432344694 |
| 8 | 3.626562 | 6.157887828748 |
| 16 | 2.308023 | 9.675796991624434 |
| 32 | 2.746344 | 8.131523946016959 |
| 64 | 2.753479 | 8.11045299419389 |
| 128 | 2.749189 | 8.123109033245806 |
| 256 | 2.757317 | 8.09916378856693 |

### 3840 × 2160

| MPI processes | Time (s) | Reported speedup |
| ---: | ---: | ---: |
| 1 | 95.935687 | 1 |
| 2 | 95.556062 | 1.0039727987116087 |
| 4 | 31.966935 | 3.0010911900061736 |
| 8 | 14.563692 | 6.5873191358345125 |
| 16 | 8.351793 | 11.486837257580497 |
| 32 | 4.976401 | 19.27812630051316 |
| 64 | 5.566426 | 17.234700865510472 |
| 128 | 5.593185 | 17.152246349798908 |
| 256 | 5.591684 | 17.156850601714975 |

For 3840 × 2160, the lowest recorded time is 4.976401 s at 32 processes, corresponding to the report's 19.27812630051316× ratio. At 64-256 processes, the archived times are approximately 5.57-5.59 s. These results describe this historical workload and environment only.

## Additional low-iteration record

The separate [100-iteration timing log](mpi-100-iterations-original.txt) records 720 × 720, `maxIter=100`, `grainSize=32`. Its one-process value is `0.000 s`, consistent with the absence of a worker in the main implementation's one-rank path. It cannot be used as a meaningful serial baseline. The file has no 32-process entry; no missing value or speedup has been supplied.

## Provenance

The [Java report](../reports/java-mandelbrot-report.pdf) and [MPI report](../reports/mpi-mandelbrot-report.pdf) identify Lukaš Patrik Magalinski as their author and describe third-year Informatics coursework. Both PDFs were exported in May 2025. Images retain their original Lithuanian labels. The JSON manifest records source filenames, extraction methods and SHA-256 digests.
