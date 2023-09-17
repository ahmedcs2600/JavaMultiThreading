package orderedprinting;

/*
Suppose there are three threads t1, t2 and t3 prints First, t2 prints Second and t3 prints Third. The code for the class is as follows
public class OrderedPrinting {
    public void printFirst() {
        System.out.println("First"):
    }

    public void printFirst() {
       System.out.println("Second"):
    }

    public void printFirst() {
       System.out.println("Third"):
    }
}
 */

public class OrderedPrinting {

    int count = 1;

    public void printFirst() {
        synchronized (this) {
            System.out.println("First");
            count++;
            this.notifyAll();
        }

    }

    public void printSecond() throws InterruptedException {
        synchronized (this) {
            while(count != 2) {
                this.wait();
            }
            System.out.println("Second");
            count++;
            this.notifyAll();
        }
    }

    public void printThird() throws InterruptedException {
        synchronized (this) {
            while(count != 3) {
                this.wait();
            }
            System.out.println("Third");
            count++;
            this.notifyAll();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        OrderedPrinting orderedPrinting = new OrderedPrinting();

        OrderedPrintingThread t1 = new OrderedPrintingThread(orderedPrinting, "first");
        OrderedPrintingThread t2 = new OrderedPrintingThread(orderedPrinting, "second");
        OrderedPrintingThread t3 = new OrderedPrintingThread(orderedPrinting, "third");

        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();
    }

    static class OrderedPrintingThread extends Thread {
        private OrderedPrinting obj;
        private String method;

        public OrderedPrintingThread(OrderedPrinting obj, String method) {
            this.obj = obj;
            this.method = method;
        }

        @Override
        public void run() {
            if("first".equals(method)) {
                obj.printFirst();
            } else if("second".equals(method)) {
                try {
                    obj.printSecond();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else if("third".equals(method)) {
                try {
                    obj.printThird();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
