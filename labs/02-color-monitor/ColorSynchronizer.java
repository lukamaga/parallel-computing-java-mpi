public class ColorSynchronizer {
    private int activeWhite = 0;
    private int activeRed   = 0;

    public synchronized void enterWhite() throws InterruptedException {
        while (activeRed > 0) {
            wait();
        }
        activeWhite++;
    }
    
    public synchronized void leaveWhite() {
        activeWhite--;
        if (activeWhite == 0) {
            notifyAll();
        }
    }

    public synchronized void enterRed() throws InterruptedException {
        while (activeWhite > 0) {
            wait();
        }
        activeRed++;
    }

    public synchronized void leaveRed() {
        activeRed--;
        if (activeRed == 0) {
            notifyAll();
        }
    }
}
