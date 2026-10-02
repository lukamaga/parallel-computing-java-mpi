#!/bin/bash
#SBATCH --job-name=mandelbrot
#SBATCH --ntasks=8
#SBATCH --time=00:10:00
#SBATCH --output=slurm-%j.out

# Submit from the repository root: sbatch hpc/submit.slurm
# Select any required account/partition using your site's sbatch options.
# Load your site's MPI compiler/runtime if it is not already available:
# module load openmpi

set -eu
cd "${SLURM_SUBMIT_DIR:?Submit from the repository root}"
make mpi

# 1080 rows / 32 rows per block gives 34 blocks.
# This source requires 2 <= total MPI ranks <= 35 for these parameters.
mpirun -np "${SLURM_NTASKS:-8}" ./build/lab4/mandelbrot_mpi \
    1920 1080 10000 32 build/lab4/mandelbrot.ppm
