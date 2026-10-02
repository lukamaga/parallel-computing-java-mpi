# Archived MPI source variant

`mpi/mandelbrot-serial-variant.c` is the complete source saved in the coursework archive as `lyg_sk4/malderbrot.txt`.

Compared with the primary [MPI lab](../labs/04-mandelbrot-mpi), it factors pixel computation into `render_block()` and adds a serial path for one MPI rank. It also prints time with six decimal places. Those features are consistent with the one-process baseline and timing precision in the historical report, but they do not establish that this was the exact revision used for every reported measurement.

The multi-process path still requires every worker to receive an initial row block. It does not explain the report's runs with more workers than blocks. This file is retained as an alternative historical version, not designated as the final submitted implementation. The root Makefile builds the primary lab source only.

The separately saved `mandelbrot1.txt` differs from the primary C source only in comments and whitespace, so it is not duplicated here.
