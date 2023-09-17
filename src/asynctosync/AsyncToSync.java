package asynctosync;

/*
Imagine we have an Executor class that performs some useful task asynchronously via the method asynchronousExecution().
In addition the method accepts a callback object which implements the Callback interface. the object's done() gets invoked when the asynchronous execution is done.
The definition for the involved classes is below
 */

public class AsyncToSync {
    public static void main(String[] args) {
        Executor executor = new Executor();
        executor.asynchronousExecution(() -> {
            System.out.println("I am done");
        });
        System.out.println("main thread exiting...");
    }

    interface Callback {
        public void done();
    }

    public static class Executor {
        public void asynchronousExecution(Callback callback) {
            Thread t = new Thread(() -> {
                try {
                    Thread.sleep(5000L);
                } catch (InterruptedException ie) {
                }
                callback.done();
            });
        }
    }

    public static class SyncExecutor extends Executor {
        @Override
        public void asynchronousExecution(Callback callback) {
            Object lock = new Object();
            final boolean[] isDone = {false};
            Callback callback1 = () -> {
                callback.done();
                synchronized (lock) {
                    isDone[0] = true;
                    lock.notify();
                }
            };
            super.asynchronousExecution(callback1);
            synchronized (lock) {
                try {
                    while (!isDone[0]) {
                        lock.wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
