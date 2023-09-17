package printnumberseries;

import java.util.concurrent.Semaphore;

/*
Suppose we are given a number n based on which a program creates the series 010203..0n.
There are three threads t1, t2 & t3 which print a specific type of number from the series.
t1 only prints zeros, t2 prints odd numbers & t3 prints even numbers from the series.
The code for the class is given as follows

public class PrintNumberSeries {
    private final int n;

    public PrintNumberSeries(int n) {
        this.n = n;
    }

    public void printZero() {

    }

    public void printOdd() {

    }

    public void printEven() {

    }
}
 */
public class PrintNumberSeries {
    private final int n;
    private Semaphore zeroSem = new Semaphore(1);
    private Semaphore oddSem = new Semaphore(0);
    private Semaphore evenSem = new Semaphore(0);

    public PrintNumberSeries(int n) {
        this.n = n;
    }

    public void printZero() {
        for(int  i = 0; i < n; i++) {
            try {
                zeroSem.acquire();
                System.out.print("0");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            (i % 2 == 0 ? oddSem : evenSem).release();
        }
    }

    public void printOdd() {
        for(int i = 1; i <= n; i+=2) {
            try {
                oddSem.acquire();
                System.out.print(i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            zeroSem.release();
        }
    }

    public void printEven() {
        for(int i = 2; i <= n; i+=2) {
            try {
                evenSem.acquire();
                System.out.print(i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            zeroSem.release();
        }
    }
}
