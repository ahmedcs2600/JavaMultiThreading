package orderedprinting;

import java.util.concurrent.CountDownLatch;

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
public class OrderedPrintingCountDownLatch {

    CountDownLatch c1 = new CountDownLatch(1);
    CountDownLatch c2 = new CountDownLatch(1);

    public void printFirst() {
        System.out.println("First");
        c1.countDown();

    }

    public void printSecond() throws InterruptedException {
        c1.await();
        System.out.println("Second");
        c2.countDown();
    }

    public void printThird() throws InterruptedException {
        c2.await();
        System.out.println("Third");
    }

    public static void main(String[] args) throws InterruptedException {
        OrderedPrintingCountDownLatch orderedPrinting = new OrderedPrintingCountDownLatch();

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
        private OrderedPrintingCountDownLatch obj;
        private String method;

        public OrderedPrintingThread(OrderedPrintingCountDownLatch obj, String method) {
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
