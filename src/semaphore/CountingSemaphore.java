package semaphore;

public class CountingSemaphore {

    private final int maxPermits;
    private int usedPermits = 0;

    CountingSemaphore(int maxPermits) {
        this.maxPermits = maxPermits;
    }

    public synchronized void acquire() throws InterruptedException {
        while(usedPermits == maxPermits) {
            wait();
        }
        usedPermits++;
        notify();
    }

    public synchronized void release() throws InterruptedException {
        while(usedPermits == 0) {
            wait();
        }
        //It doesn't matter if we switch the lines since other function will be called when this function exit from Monitor
        usedPermits--;
        notify();
    }
}
