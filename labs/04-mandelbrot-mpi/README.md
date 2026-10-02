# Lab 4: Mandelbrot with MPI

This C implementation uses a coordinator/worker model. Rank 0 assigns row blocks, receives their RGB bytes and sends the next available block to the worker that finishes. Ranks 1 through P-1 compute pixels. Rank 0 writes the assembled image as a binary PPM file.

The complex-plane bounds are the same as in the Java lab, but this version uses a grayscale colour map. The PNG and PPM outputs are therefore not expected to have identical pixel colours.

## Build

An MPI implementation with `mpicc` and `mpirun`, such as Open MPI or MPICH, and a C compiler are required. No additional C libraries are used.

From the repository root:

```sh
make mpi
```

Without Make:

```sh
mkdir -p build/lab4
mpicc -O3 -std=c11 -Wall -Wextra labs/04-mandelbrot-mpi/mandelbrot_mpi.c -o build/lab4/mandelbrot_mpi
```

## Run with valid process and block counts

```sh
mpirun -np 4 ./build/lab4/mandelbrot_mpi 720 720 1000 16 build/lab4/mandelbrot.ppm
```

This uses one coordinator and three workers. The 720 rows divided into blocks of 16 produce 45 tasks, so every worker receives initial work.

| Argument | Meaning | Example |
| --- | --- | --- |
| `width` | Image width | `720` |
| `height` | Image height | `720` |
| `maxIter` | Iteration limit per pixel | `1000` |
| `grainSize` | Rows in one task | `16` |
| `output.ppm` | Output path; parent directory must exist | `build/lab4/mandelbrot.ppm` |

All five program arguments are required. The MPI rank count is supplied to `mpirun`, not to the C program. Rank 0 reports total processing time and process count. Timing covers task distribution and collection, excluding allocation before the timer and PPM writing afterwards.

For the 1920 x 1080 workload used in the coursework material, a valid example is:

```sh
mpirun -np 8 ./build/lab4/mandelbrot_mpi 1920 1080 10000 32 build/lab4/mandelbrot-hd.ppm
```

Open the result with an image viewer that supports PPM. Existing output files are overwritten. If the MPI runtime reports insufficient slots, choose fewer processes while keeping the constraints below; cluster allocation and launcher options depend on the local installation.

## Process and input constraints

Use all of these conditions:

- At least two MPI processes: rank 0 coordinates and at least one worker computes.
- No more workers than row blocks: `processes - 1 <= ceil(height / grainSize)`.
- Width and height greater than one; positive `maxIter` and `grainSize`.
- Manageable dimensions and block sizes, with products fitting C `int` counts, available memory and MPI message limits.
- An existing, writable output directory.

The source does not validate these conditions. With one rank, no worker computes the image. With more workers than initial blocks, idle workers wait for a message they never receive. The implementation also does not check allocation failures or `fopen` errors.

For `height=1080` and `grainSize=32`, there are 34 blocks, so the supported process-count range is 2 through 35. Requests for 64, 128 or 256 ranks do not satisfy this version's worker-assignment constraint.

## Historical scripts and measurements

The archive includes Slurm examples `go.sh` and `run.sh`. Their compiler and launcher commands informed the instructions above, but the scripts are not copied as ready-to-run automation. They contain site-specific scheduling choices; the sweep also uses a one-rank baseline and process counts outside this saved implementation's valid range. Its `%N`-based shell timing is platform-dependent.

The [archived serial-path variant](../../archive/README.md) contains an explicit one-rank implementation and six-decimal timing output. It partly explains the report's baseline and precision, but its multi-process path has the same worker/block constraint. The exact source revision behind every historical measurement has not been established.

## Slurm submission template

The repository includes a [submission template](../../hpc/submit.slurm) for eight ranks, 1920 x 1080 pixels, 10,000 iterations and 32 rows per block. From the repository root:

```sh
sbatch hpc/submit.slurm
```

The template compiles with `make mpi` inside the allocation, then launches the renderer and writes `build/lab4/mandelbrot.ppm`. Set any required partition/account through your site's `sbatch` options and uncomment or adjust the MPI module line if needed. If changing the rank count or workload, retain the process/block conditions above. This is a repository submission helper, separate from the original archived scripts.

## Files

- `mandelbrot_mpi.c`: original row-task protocol, pixel kernel and PPM writer.
