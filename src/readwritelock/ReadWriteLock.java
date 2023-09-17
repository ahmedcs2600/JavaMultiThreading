package readwritelock;

public class ReadWriteLock {
    boolean isWriteInProgress = false;
    int readers = 0;

    public synchronized void acquireReadLock() throws InterruptedException {
        while(isWriteInProgress) {
            wait();
        }
        readers++;
    }

    public synchronized void releaseReadLock() throws InterruptedException {
        readers--;
        if(readers == 0) notify();
    }

    public synchronized void acquireWriteLock() throws InterruptedException {
        while(readers > 0 || isWriteInProgress) {
            wait();
        }
        isWriteInProgress = true;
        notify();
    }

    public synchronized void releaseWriteLock() {
        isWriteInProgress = false;
        notify();
    }

    public static void main(String[] args) {
        ReadWriteLock readWriteLock = new ReadWriteLock();

        Thread t1 = new Thread(() -> {
            try {
                readWriteLock.acquireWriteLock();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for(int i = 0 ; i <= Integer.MAX_VALUE ; i++) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            readWriteLock.releaseWriteLock();
        });
    }
}
