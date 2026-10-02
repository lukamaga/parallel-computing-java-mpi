#include <mpi.h>
#include <stdio.h>
#include <stdlib.h>
#include <math.h>

// Tags
#define TAG_TASK   1
#define TAG_RESULT 2
#define TAG_STOP   3

// Mandelbrot spalvinimas (grayscale)
void color_map(int iter, int maxIter, unsigned char *r, unsigned char *g, unsigned char *b) {
    if (iter >= maxIter) {
        *r = *g = *b = 0;
    } else {
        int c = (int)(255.0 * iter / maxIter);
        *r = *g = *b = c;
    }
}

// Renderavimo funkcija vienam blokui eilučių [rowStart .. rowStart+chunk)
void render_block(int rowStart, int chunk, int width, int height,
                  int maxIter, double xMin, double xMax,
                  double yMin, double yMax,
                  unsigned char *outBuf) {
    for (int dy = 0; dy < chunk; ++dy) {
        int y = rowStart + dy;
        double cy = yMin + (yMax - yMin) * y / (height - 1);
        for (int x = 0; x < width; ++x) {
            double cx = xMin + (xMax - xMin) * x / (width - 1);
            double zx = 0.0, zy = 0.0;
            int iter = 0;
            while (zx*zx + zy*zy <= 4.0 && iter < maxIter) {
                double nx = zx*zx - zy*zy + cx;
                zy = 2.0*zx*zy + cy;
                zx = nx;
                iter++;
            }
            unsigned char r,g,b;
            color_map(iter, maxIter, &r,&g,&b);
            int idx = 3*(dy*width + x);
            outBuf[idx]   = r;
            outBuf[idx+1] = g;
            outBuf[idx+2] = b;
        }
    }
}

int main(int argc, char *argv[]) {
    MPI_Init(&argc, &argv);
    int rank, size;
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &size);

    // Argumentų patikra
    if (argc < 6) {
        if (rank==0) {
            fprintf(stderr,
                "Usage: %s <width> <height> <maxIter> <grainSize> <output.ppm>\n",
                argv[0]);
        }
        MPI_Finalize();
        return 1;
    }

    // Parametrai
    int width     = atoi(argv[1]);
    int height    = atoi(argv[2]);
    int maxIter   = atoi(argv[3]);
    int grainSize = atoi(argv[4]);
    const char *outFilename = argv[5];

    // Kompleksinės plokštumos ribos
    double xMin=-2.0, xMax=1.0, yMin=-1.5, yMax=1.5;

    // Vieno proceso (serijinis) režimas
    if (size == 1) {
        unsigned char *image = malloc(3*width*height);
        double t0 = MPI_Wtime();
        // Renderinti visą vaizdą blokais
        for (int row=0; row<height; row+=grainSize) {
            int chunk = grainSize;
            if (row+chunk > height) chunk = height-row;
            render_block(row, chunk, width, height,
                         maxIter, xMin,xMax, yMin,yMax,
                         &image[3*width*row]);
        }
        double t1 = MPI_Wtime();
        printf("Total time: %.6f s with %d processes\n", t1-t0, size);
        // Išsaugoti PPM
        FILE *f = fopen(outFilename,"wb");
        fprintf(f,"P6\n%d %d\n255\n", width, height);
        fwrite(image,1,3*width*height,f);
        fclose(f);
        free(image);
        MPI_Finalize();
        return 0;
    }

    // Daugiau nei vienas procesas – master–worker modelis
    if (rank == 0) {
        // Master
        unsigned char *image = malloc(3*width*height);
        int nextRow = 0, pending = 0;
        double t0 = MPI_Wtime();

        // Paskirstome pirmai batch workeriams
        for (int p = 1; p < size && nextRow < height; ++p) {
            MPI_Send(&nextRow, 1, MPI_INT, p, TAG_TASK, MPI_COMM_WORLD);
            nextRow += grainSize;
            pending++;
        }

        // Gauname rezultatą ir dalijame tolimesnius darbus
        while (pending > 0) {
            MPI_Status st;
            int rowStart;
            // 1) gauname block id
            MPI_Recv(&rowStart, 1, MPI_INT, MPI_ANY_SOURCE,
                     TAG_RESULT, MPI_COMM_WORLD, &st);
            int src = st.MPI_SOURCE;
            int chunk = grainSize;
            if (rowStart + chunk > height) chunk = height - rowStart;
            // 2) gauname spalvų duomenis
            MPI_Recv(&image[3*width*rowStart], 3*width*chunk,
                     MPI_UNSIGNED_CHAR,
                     src, TAG_RESULT, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
            pending--;

            // Siunčiame naują darbą arba STOP
            if (nextRow < height) {
                MPI_Send(&nextRow,1,MPI_INT,src,TAG_TASK,MPI_COMM_WORLD);
                nextRow += grainSize;
                pending++;
            } else {
                int stop = -1;
                MPI_Send(&stop,1,MPI_INT,src,TAG_STOP,MPI_COMM_WORLD);
            }
        }

        double t1 = MPI_Wtime();
        printf("Total time: %.6f s with %d processes\n", t1-t0, size);
        // Įrašome PPM
        FILE *f = fopen(outFilename,"wb");
        fprintf(f, "P6\n%d %d\n255\n", width, height);
        fwrite(image,1,3*width*height,f);
        fclose(f);
        free(image);
    } else {
        // Worker
        while (1) {
            MPI_Status st;
            int rowStart;
            MPI_Recv(&rowStart,1,MPI_INT,0,MPI_ANY_TAG,
                     MPI_COMM_WORLD,&st);
            if (st.MPI_TAG == TAG_STOP || rowStart<0) break;
            int chunk = grainSize;
            if (rowStart+chunk > height) chunk = height-rowStart;
            unsigned char *buffer = malloc(3*width*chunk);
            // Renderinamas blokas
            render_block(rowStart,chunk,width,height,
                         maxIter,xMin,xMax,yMin,yMax,buffer);
            // Siunčiame atgal master'iui
            MPI_Send(&rowStart,1,MPI_INT,0,TAG_RESULT,MPI_COMM_WORLD);
            MPI_Send(buffer,3*width*chunk,
                     MPI_UNSIGNED_CHAR,0,TAG_RESULT,MPI_COMM_WORLD);
            free(buffer);
        }
    }

    MPI_Finalize();
    return 0;
}
