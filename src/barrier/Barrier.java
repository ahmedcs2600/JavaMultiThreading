package barrier;

/*
A barrier can be thought of as a point in the program code, which all or some of the threads need to reach at before any one of them is allowed to proceed
 */

public class Barrier {
    int count = 0;
    int totalThreads;
    int release = 0;

    public Barrier(int totalThreads) {
        this.totalThreads = totalThreads;
    }

    public synchronized void await() throws InterruptedException {
        while (count == totalThreads) wait();
        count++;
        if (count == totalThreads) {
            notifyAll();
            release = totalThreads;
        } else {
            while (count < totalThreads) {
                wait();
            }
        }
        release--;
        if (release == 0) {
            count = 0;
            notifyAll();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        final Barrier barrier = new Barrier(3);
        Thread t1 = new Thread(() -> {
            try {
                System.out.println("Thread 1");
                barrier.await();
                System.out.println("Thread 1");
                barrier.await();
                System.out.println("Thread 1");
                barrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                Thread.sleep(500);
                System.out.println("Thread 2");
                barrier.await();
                Thread.sleep(500);
                System.out.println("Thread 2");
                barrier.await();
                Thread.sleep(500);
                System.out.println("Thread 2");
                barrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread t3 = new Thread(() -> {
            try {
                Thread.sleep(1500);
                System.out.println("Thread 3");
                barrier.await();
                Thread.sleep(1500);
                System.out.println("Thread 3");
                barrier.await();
                Thread.sleep(1500);
                System.out.println("Thread 3");
                barrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();
    }
}
