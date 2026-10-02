import java.util.concurrent.atomic.AtomicInteger;


public class TTestMandelbrot extends Thread {
    volatile boolean finished = false;

    // Global parameters for Mandelbrot test
    static int nThreads;
    static int width, height, maxIter, grainSize;
    static AtomicInteger rowCounter;
    static long workload;

    // Complex plane bounds
    static final double xMin = -2.0, xMax = 1.0;
    static final double yMin = -1.5, yMax = 1.5;

    public static void main(String[] args) throws Exception {
        if (args.length < 4) {
            System.err.println("Usage: java TTestMandelbrot <width> <height> <maxIter> <grainSize>");
            System.exit(1);
        }
        width = Integer.parseInt(args[0]);
        height = Integer.parseInt(args[1]);
        maxIter = Integer.parseInt(args[2]);
        grainSize = Integer.parseInt(args[3]);
        workload = (long) width * height;

        System.out.println("#nThreads #workload #timeS #speedup");
        double t1 = 0;
        for (int threads = 1; threads <= 32; threads *= 2) {
            double t = makeTest(threads);
            if (threads == 1) t1 = t;
            double speedup = t1 / t;
            System.out.printf("%d %d %.3f %f%n", threads, workload, t, speedup);
        }
        System.out.println("#completed");
    }

    static double makeTest(int threads) throws InterruptedException {
        nThreads = threads;
        rowCounter = new AtomicInteger(0);

        long t0 = System.currentTimeMillis();
        TTestMandelbrot[] ths = new TTestMandelbrot[nThreads];
        for (int i = 0; i < nThreads; i++) {
            ths[i] = new TTestMandelbrot();
            ths[i].start();
        }
        for (int i = 0; i < nThreads; i++) {
            while (!ths[i].finished) {
                ths[i].join();
            }
        }
        long t1 = System.currentTimeMillis();
        return (t1 - t0) / 1000.0;
    }

    @Override
    public void run() {
        while (true) {
            int yStart = rowCounter.getAndAdd(grainSize);
            if (yStart >= height) break;
            int yEnd = Math.min(yStart + grainSize, height);
            for (int y = yStart; y < yEnd; y++) {
                double cy = yMin + y * (yMax - yMin) / (height - 1);
                for (int x = 0; x < width; x++) {
                    double cx = xMin + x * (xMax - xMin) / (width - 1);
                    mandelbrotIterations(cx, cy);
                }
            }
        }
        finished = true;
    }

    private int mandelbrotIterations(double cx, double cy) {
        double zx = 0, zy = 0;
        int iter = 0;
        while (zx * zx + zy * zy <= 4.0 && iter < maxIter) {
            double tmp = zx * zx - zy * zy + cx;
            zy = 2.0 * zx * zy + cy;
            zx = tmp;
            iter++;
        }
        return iter;
    }
}
