public class ColorDemo {
    private static final int WHITE_THREAD_COUNT = 5;
    private static final int RED_THREAD_COUNT   = 5;

    public static void main(String[] args) {
        ColorSynchronizer synchronizer = new ColorSynchronizer();

        for (int i = 1; i <= WHITE_THREAD_COUNT; i++) {
            Thread whiteThread = new Thread(new WhiteTask(synchronizer, i));
            whiteThread.start();
        }

        for (int i = 1; i <= RED_THREAD_COUNT; i++) {
            Thread redThread = new Thread(new RedTask(synchronizer, i));
            redThread.start();
        }
    }

    static class WhiteTask implements Runnable {
        private final ColorSynchronizer synchronizer;
        private final int threadId;

        public WhiteTask(ColorSynchronizer synchronizer, int threadId) {
            this.synchronizer = synchronizer;
            this.threadId = threadId;
        }

        @Override
        public void run() {
            try {
                synchronizer.enterWhite();
                System.out.println("Baltoji gija #" + threadId + " įžengė į kritinę sekciją.");

                Thread.currentThread().sleep(0);

                System.out.println("Baltoji gija #" + threadId + " baigia darbą kritinėje sekcijoje.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                synchronizer.leaveWhite();
            }
        }
    }

    static class RedTask implements Runnable {
        private final ColorSynchronizer synchronizer;
        private final int threadId;

        public RedTask(ColorSynchronizer synchronizer, int threadId) {
            this.synchronizer = synchronizer;
            this.threadId = threadId;
        }

        @Override
        public void run() {
            try {
                synchronizer.enterRed();
                System.out.println("Raudonoji gija #" + threadId + " įžengė į kritinę sekciją.");

                Thread.sleep((long) (500 + Math.random() * 1000));

                System.out.println("Raudonoji gija #" + threadId + " baigia darbą kritinėje sekcijoje.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                synchronizer.leaveRed();
            }
        }
    }
}
