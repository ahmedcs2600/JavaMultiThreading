package blockingqueue;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BlockingQueueMutex {
    private final int size;
    private final int[] queue;
    private int head;
    private int tail;
    private int currentSize;
    private Lock lock = new ReentrantLock();

    BlockingQueueMutex(int size) {
        this.size = size;
        this.currentSize = 0;
        queue = new int[size];
    }

    public void enqueue(int element) {
        lock.lock();

        while (currentSize == size) {
            lock.unlock();
            lock.lock();
        }

        queue[tail] = element;
        tail = (tail + 1) % size;
        currentSize++;
        lock.unlock();
    }

    public int dequeue() {
        lock.lock();
        while (currentSize <= 0) {
            lock.unlock();
            lock.lock();
        }

        int res = queue[head];
        head = (head + 1) % size;
        currentSize--;
        lock.unlock();
        return res;
    }
}
