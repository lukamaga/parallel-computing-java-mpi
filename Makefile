# Build helpers only. No target executes the lab programs.
JAVAC ?= javac
MPICC ?= mpicc
CFLAGS ?= -O3 -std=c11 -Wall -Wextra

.PHONY: help java java-lab1 java-lab2 java-lab3 mpi

help:
	@printf '%s\n' 'make java       Compile the three Java labs into build/lab1, lab2, lab3.' 'make mpi        Compile the MPI renderer into build/lab4.' 'See each lab README for execution commands and parameter constraints.'

java: java-lab1 java-lab2 java-lab3

java-lab1:
	mkdir -p build/lab1
	$(JAVAC) -encoding UTF-8 -d build/lab1 labs/01-warehouse-race/Main.java

java-lab2:
	mkdir -p build/lab2
	$(JAVAC) -encoding UTF-8 -d build/lab2 labs/02-color-monitor/ColorSynchronizer.java labs/02-color-monitor/ColorDemo.java

java-lab3:
	mkdir -p build/lab3
	$(JAVAC) -encoding UTF-8 -d build/lab3 labs/03-mandelbrot-java/Mandelbrot.java labs/03-mandelbrot-java/TTestMandelbrot.java

mpi:
	mkdir -p build/lab4
	$(MPICC) $(CFLAGS) labs/04-mandelbrot-mpi/mandelbrot_mpi.c -o build/lab4/mandelbrot_mpi
