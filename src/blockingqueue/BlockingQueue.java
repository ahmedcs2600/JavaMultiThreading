package blockingqueue;

public class BlockingQueue {
    private final int size;
    private final int[] queue;
    private int head;
    private int tail;
    private int currentSize;

    public BlockingQueue(int size) {
        this.size = size;
        this.currentSize = 0;
        queue = new int[size];
    }

    public synchronized void enqueue(int element) throws InterruptedException {
        while (currentSize == size) {
            wait();
        }

        queue[tail] = element;
        tail = (tail + 1) % size;
        currentSize++;
        notifyAll();
    }

    public synchronized int dequeue() throws InterruptedException {
        while (currentSize <= 0) {
            wait();
        }

        int res = queue[head];
        head = (head + 1) % size;
        currentSize--;
        notifyAll();
        return res;
    }
}
