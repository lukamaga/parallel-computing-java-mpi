# Original figure gallery

These original illustrations accompany the [Java report](../reports/java-mandelbrot-report.pdf) and [MPI report](../reports/mpi-mandelbrot-report.pdf). Source filenames and SHA-256 digests are recorded in [historical-results.json](historical-results.json). The two MPI images are the embedded PNGs from the author's DOCX report.

Read the [historical results and caveats](historical-results.md) before comparing numbers: the Java table and chart series differ, and the MPI report does not identify the code revision used for each measurement. The main MPI source needs at least two ranks; the [archived variant](../../archive/mpi/mandelbrot-serial-variant.c) includes a serial path.

## Java runtime by threads

![Java rendering time by thread count; 1920 × 1080, maxIter=100000, grainSize=32. Historical chart labels differ from the adjacent report table.](../assets/java-runtime-by-threads.png)

Java rendering time by thread count; 1920 × 1080, maxIter=100000, grainSize=32. Historical chart labels differ from the adjacent report table.

Source: `lyg_sk3/lyg_sk3_graphs/myplot.png`; [Java report](../reports/java-mandelbrot-report.pdf), page 2.

## Java speedup by threads

![Java speedup by thread count, with ideal S(p)=p reference; 1920 × 1080, maxIter=100000, grainSize=32. Uses the chart series, not the report table series.](../assets/java-speedup-by-threads.png)

Java speedup by thread count, with ideal S(p)=p reference; 1920 × 1080, maxIter=100000, grainSize=32. Uses the chart series, not the report table series.

Source: `lyg_sk3/lyg_sk3_graphs/myplot2.png`; [Java report](../reports/java-mandelbrot-report.pdf), page 3.

## Java efficiency by threads

![Java parallel efficiency E(p)=S(p)/p for 1, 2, 4, 8, 16 and 32 threads; 1920 × 1080, maxIter=100000, grainSize=32. Uses the chart series.](../assets/java-efficiency-by-threads.png)

Java parallel efficiency E(p)=S(p)/p for 1, 2, 4, 8, 16 and 32 threads; 1920 × 1080, maxIter=100000, grainSize=32. Uses the chart series.

Source: `lyg_sk3/lyg_sk3_graphs/myplot3.png`; [Java report](../reports/java-mandelbrot-report.pdf), page 3.

## Java speedup by iteration limit

![Java speedup for maxIter=1000, 2000, 5000, 10000, 50000 and 100000 at grainSize=32. The report does not explicitly restate image dimensions for this sweep.](../assets/java-speedup-by-iteration-limit.png)

Java speedup for maxIter=1000, 2000, 5000, 10000, 50000 and 100000 at grainSize=32. The report does not explicitly restate image dimensions for this sweep.

Source: `lyg_sk3/lyg_sk3_graphs/myplot4.png`; [Java report](../reports/java-mandelbrot-report.pdf), page 4.

## Java runtime by grain size

![Java runtime versus block height (1, 2, 4, 16, 32, 64, 128, 256 rows) at maxIter=10000; series show 1-32 threads. Image dimensions are not explicitly restated for this sweep.](../assets/java-runtime-by-grain-size.png)

Java runtime versus block height (1, 2, 4, 16, 32, 64, 128, 256 rows) at maxIter=10000; series show 1-32 threads. Image dimensions are not explicitly restated for this sweep.

Source: `lyg_sk3/lyg_sk3_graphs/myplot5.png`; [Java report](../reports/java-mandelbrot-report.pdf), page 4.

## Java speedup by grain size

![Java speedup versus block height at maxIter=10000; series show 1-32 threads. The x-axis is grainSize, not thread count. Image dimensions are not explicitly restated for this sweep.](../assets/java-speedup-by-grain-size.png)

Java speedup versus block height at maxIter=10000; series show 1-32 threads. The x-axis is grainSize, not thread count. Image dimensions are not explicitly restated for this sweep.

Source: `lyg_sk3/lyg_sk3_graphs/myplot6.png`; [Java report](../reports/java-mandelbrot-report.pdf), page 5.

## Java mandelbrot 1920x1080

![Preserved Java Mandelbrot rendering, 1920 × 1080 pixels. The saved image does not establish its exact maxIter, grainSize or thread count.](../assets/java-mandelbrot-1920x1080.png)

Preserved Java Mandelbrot rendering, 1920 × 1080 pixels. The saved image does not establish its exact maxIter, grainSize or thread count.

Source: `lyg_sk3/src/mandelbrot.png`; Java source output, also shown on [Java report page 5](../reports/java-mandelbrot-report.pdf).

## Mpi speedup by image size

![Historical MPI speedup T(1)/T(p) for 720 × 720, 1920 × 1080 and 3840 × 2160 images; maxIter=10000, grainSize=32, 1-256 reported processes. The one-process reference is historical; the main implementation has no serial rendering path, while the archived variant does. The exact revision behind the measurements is not recorded.](../assets/mpi-speedup-by-image-size.png)

Historical MPI speedup T(1)/T(p) for 720 × 720, 1920 × 1080 and 3840 × 2160 images; maxIter=10000, grainSize=32, 1-256 reported processes. The one-process reference is historical; the main implementation has no serial rendering path, while the archived variant does. The exact revision behind the measurements is not recorded.

Source: `lyg_sk4/Ataskaita_2.docx: word/media/image1.png`; [MPI report](../reports/mpi-mandelbrot-report.pdf), page 2.

## Mpi mandelbrot 720x720

![Preserved MPI Mandelbrot rendering, 720 × 720 pixels. Extracted from the report; its RGB pixels exactly match the saved mandelbrot.ppm. The precise run parameters are not established by the image.](../assets/mpi-mandelbrot-720x720.png)

Preserved MPI Mandelbrot rendering, 720 × 720 pixels. Extracted from the report; its RGB pixels exactly match the saved mandelbrot.ppm. The precise run parameters are not established by the image.

Source: `lyg_sk4/Ataskaita_2.docx: word/media/image2.png`; [MPI report](../reports/mpi-mandelbrot-report.pdf), page 3.

The MPI image uses a grayscale palette and the Java image uses a color palette. They are different preserved outputs, not controlled comparisons of identical rendering parameters. The extracted 720 × 720 MPI PNG has the same RGB pixel data as the archive's `lyg_sk4/mandelbrot.ppm`; the larger PPM was omitted to avoid storing duplicate imagery.
