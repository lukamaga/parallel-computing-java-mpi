# Lab 3: Mandelbrot with Java threads

This lab divides image rows into blocks and assigns them dynamically to worker threads. Each worker claims the next block with `AtomicInteger.getAndAdd(grainSize)`, computes its pixels and then requests more rows until the image is complete.

The complex plane spans x from -2.0 to 1.0 and y from -1.5 to 1.5. Pixels whose iteration count reaches the limit are black; other pixels use a hue derived from that count. Smaller row blocks give finer scheduling granularity, while larger blocks reduce the number of work claims.

## Build

From the repository root, with a JDK installed:

```sh
make java-lab3
```

Without Make:

```sh
mkdir -p build/lab3
javac -encoding UTF-8 -d build/lab3 labs/03-mandelbrot-java/Mandelbrot.java labs/03-mandelbrot-java/TTestMandelbrot.java
```

Only JDK classes are used, including `BufferedImage` and `ImageIO`. The programs do not open a graphical window.

## Render an image

```sh
java -cp build/lab3 Mandelbrot 4 720 720 1000 16 fast
```

The program writes `mandelbrot.png` to the current working directory and overwrites an existing file of that name. It prints the elapsed rendering time before writing the image, so the reported time excludes PNG encoding and file output.

| Argument | Meaning | Example |
| --- | --- | --- |
| `nThreads` | Worker thread count | `4` |
| `width` | Image width in pixels | `720` |
| `height` | Image height in pixels | `720` |
| `maxIter` | Iteration limit per pixel | `1000` |
| `grainSize` | Rows claimed at once | `16` |
| `debug` or `fast` | Console detail/output mode | `fast` |

All six arguments are required; there are no numeric defaults. For meaningful runs, use a positive thread count, dimensions greater than one, and positive iteration/grain values. The source parses integers but does not enforce those ranges. In particular, zero grain size can prevent progress.

For a small scheduling trace:

```sh
java -cp build/lab3 Mandelbrot 4 80 60 100 8 debug
```

Debug mode prints each worker's row interval and a completion summary. It does not write a PNG. Only the word `debug` enables that mode; any other sixth argument falls through to the image-writing path, so use the documented `fast` spelling.

## Original timing driver

```sh
java -cp build/lab3 TTestMandelbrot 720 720 1000 16
```

Its arguments are `width height maxIter grainSize`. It tries thread counts 1, 2, 4, 8, 16 and 32, printing workload (`width * height`), seconds and speedup relative to its one-thread measurement. It computes iteration loops without producing an image.

This is the original coursework timing driver, not a benchmark harness with warm-up, repeated samples or statistical analysis. It uses millisecond timing, so very short measurements can be zero. It also discards the returned iteration counts; JVM optimization may affect the workload. Do not interpret a single run as a reliable comparison between machines or as a new validation of the historical charts.

The timing-driver structure follows the course `TTest` pattern by R. Vaicekauskas. See [attribution](../../ATTRIBUTION.md).

## Files

- `Mandelbrot.java`: threaded rendering, scheduling trace and PNG output.
- `TTestMandelbrot.java`: the saved six-point timing driver.
