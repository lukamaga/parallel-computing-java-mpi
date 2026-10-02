//sandelis, kur galima iterpti ir panaikinti prekes ir taip pat suzinoti visu prekiu galutine kaina
public class Main {

    public static void main(String[] args) throws InterruptedException {
        Warehouse wh = new Warehouse();

        Thread t1 = new Thread(() -> doWork(wh));
        Thread t2 = new Thread(() -> doWork(wh));

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("Galutinis prekių kiekis: " + wh.getItems());
        System.out.println("Galutine prekiu verte:   " + wh.getTotalValue());
        System.out.println("(Abu turetu buti 0, jei viskas atlikta teisingai.)");
    }

    private static void doWork(Warehouse wh) {
        for (int i = 0; i < 1000; i++) {

            // NEsinchronizuotas
            wh.addItems(1);
            wh.removeItems(1);

            // sinchronizuotas
           // synchronized (wh) {
           //     wh.addItems(1);
           //     wh.removeItems(1);
           // }
        }
    }

    static class Warehouse {
        private int items = 0;
        private int totalValue = 0;
        private final int ITEM_PRICE = 10; //negalim pakesiti po inicializavimo

        public void addItems(int n) {
            items += n;
            totalValue += n * ITEM_PRICE;
        }

        public void removeItems(int n) {
            items -= n;
            totalValue -= n * ITEM_PRICE;
        }

        public int getItems() {
            return items;
        }

        public int getTotalValue() {
            return totalValue;
        }
    }
}
