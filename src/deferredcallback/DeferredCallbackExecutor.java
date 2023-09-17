package deferredcallback;

import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class DeferredCallbackExecutor {
    PriorityQueue<CallBack> q = new PriorityQueue<>(Comparator.comparingLong(o -> o.executeAt));

    private ReentrantLock lock = new ReentrantLock();

    Condition newCallbackArrived = lock.newCondition();

    private long findSleepDuration() {
        long currentTime = System.currentTimeMillis();
        return q.peek().executeAt - currentTime;
    }

    public void start() throws InterruptedException {
        while (true) {
            lock.lock();

            while(q.isEmpty()) { newCallbackArrived.await(); } // While loop to cater spurious wakeups

            // While loop to cater spurious wakeups
            while(!q.isEmpty()) {
                long sleepFor = findSleepDuration();
                if(sleepFor <= 0) break;
                newCallbackArrived.await(sleepFor, TimeUnit.MILLISECONDS);
            }

            CallBack cb = q.poll();
            System.out.println("Executed at " + System.currentTimeMillis() / 1000 + " required at " + cb.executeAt + " :message:" + cb.message);
            lock.unlock();
        }
    }

    public void registerCallBack(CallBack callBack) {
        lock.lock();
        q.offer(callBack);
        newCallbackArrived.signal();
        lock.unlock();
    }

    static class CallBack {
        long executeAt;
        String message;

        public CallBack(long executeAfter, String message) {
            this.executeAt = System.currentTimeMillis() + executeAfter;
            this.message = message;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        HashSet<Thread> allThreads = new HashSet<>();
        DeferredCallbackExecutor deferredCallbackExecutor = new DeferredCallbackExecutor();
        Thread service = new Thread(() -> {
            try {
                deferredCallbackExecutor.start();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        for(int i = 0; i < 10; i++) {
            Thread thread = new Thread(() -> {
                CallBack cb = new CallBack(1, "Hello this is" + Thread.currentThread().getName());
                deferredCallbackExecutor.registerCallBack(cb);
            });
            thread.setName("Thread_" + (i + 1));
            thread.start();
            allThreads.add(thread);
            Thread.sleep(1000L);
        }


        for(Thread thread : allThreads) {
            thread.join();
        }
    }
}
