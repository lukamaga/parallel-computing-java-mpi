import java.awt.image.BufferedImage;
import java.awt.Color;
import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;
import javax.imageio.ImageIO;

public class Mandelbrot {
    // Shared parameters
    private static int nThreads;
    private static int width;
    private static int height;
    private static int maxIter;
    private static int grainSize;
    private static boolean debug;
    private static BufferedImage image;
    private static AtomicInteger rowCounter;

    // Complex plane bounds
    private static final double xMin = -2.0;
    private static final double xMax = 1.0;
    private static final double yMin = -1.5;
    private static final double yMax = 1.5;

    public static void main(String[] args) {
        if (args.length < 6) {
            System.err.println("Usage: java Mandelbrot <nThreads> <width> <height> <maxIter> <grainSize> <debug|fast>");
            System.exit(1);
        }
        try {
            nThreads  = Integer.parseInt(args[0]);
            width     = Integer.parseInt(args[1]);
            height    = Integer.parseInt(args[2]);
            maxIter   = Integer.parseInt(args[3]);
            grainSize = Integer.parseInt(args[4]);
            String mode = args[5].toLowerCase();
            debug = mode.equals("debug");
        } catch (NumberFormatException e) {
            System.err.println("Invalid numeric argument");
            System.exit(2);
        }

        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        rowCounter = new AtomicInteger(0);

        long startTime = System.nanoTime();

        Thread[] threads = new Thread[nThreads];
        for (int i = 0; i < nThreads; i++) {
            threads[i] = new Thread(new Worker(), "Worker-" + i);
            threads[i].start();
        }
        for (int i = 0; i < nThreads; i++) {
            try { threads[i].join(); } catch (InterruptedException e) { /* ignore */ }
        }

        long endTime = System.nanoTime();
        double elapsedSec = (endTime - startTime) / 1_000_000_000.0;

        if (debug) {
            System.out.printf("[DEBUG] Completed with %d threads, image %dx%d, maxIter=%d, grainSize=%d\n",
                    nThreads, width, height, maxIter, grainSize);
        } else {
            System.out.printf("Execution time: %.4f seconds\n", elapsedSec);
        }

        // Save image only in fast mode
        if (!debug) {
            try {
                File out = new File("mandelbrot.png");
                ImageIO.write(image, "png", out);
                System.out.println("Image written to mandelbrot.png");
            } catch (Exception e) {
                System.err.println("Failed to write image: " + e.getMessage());
            }
        }
    }

    static class Worker implements Runnable {
        @Override
        public void run() {
            while (true) {
                int yStart = rowCounter.getAndAdd(grainSize);
                if (yStart >= height) break;
                int yEnd = Math.min(yStart + grainSize, height);
                renderRows(yStart, yEnd);
                if (debug) {
                    System.out.printf("%s processed rows [%d..%d)%n",
                            Thread.currentThread().getName(), yStart, yEnd);
                }
            }
        }

        private void renderRows(int yStart, int yEnd) {
            for (int y = yStart; y < yEnd; y++) {
                double cy = yMin + y * (yMax - yMin) / (height - 1);
                for (int x = 0; x < width; x++) {
                    double cx = xMin + x * (xMax - xMin) / (width - 1);
                    int iter = mandelbrotIterations(cx, cy);
                    int color = iter == maxIter ? 0 : Color.HSBtoRGB((float) iter / maxIter, 1f, 1f);
                    image.setRGB(x, y, color);
                }
            }
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
}

