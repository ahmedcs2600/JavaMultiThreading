package ratelimiting;

import java.util.HashSet;
import java.util.Set;

public class TokenBucketFilterBackgroundThread {

    private final int maxTokens;
    private int possibleTokens;

    TokenBucketFilterBackgroundThread(int maxTokens) {
        this.maxTokens = maxTokens;
    }

    void daemonThread() {
        while(true) {
            synchronized (this) {
                if(possibleTokens < maxTokens) {
                    possibleTokens++;
                }
                notify();
            }

            try {
                Thread.sleep(1000L);
            } catch (InterruptedException ex) {
                //exception
            }
        }
    }

    void getToken() throws InterruptedException {
        synchronized (this) {
            while(possibleTokens == 0) {
                wait();
            }
            possibleTokens--;
        }
        System.out.println("Granting " + Thread.currentThread().getName() + " token at " + System.currentTimeMillis() / 1000);
    }


    public static void main(String[] args) throws InterruptedException {
        Set<Thread> allThreads = new HashSet<>();

        //TODO use factory pattern
        final TokenBucketFilterBackgroundThread tokenBucketFilter = new TokenBucketFilterBackgroundThread(5);

        Thread daemonThread = new Thread(new Runnable() {
            @Override
            public void run() {
                tokenBucketFilter.daemonThread();
            }
        });

        daemonThread.setDaemon(true);

        Thread.sleep(1000 * 10);

        for(int i = 0; i < 12; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        tokenBucketFilter.getToken();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            allThreads.add(thread);
        }
        for(Thread t: allThreads) {
            t.start();
        }

        for(Thread t: allThreads) {
            t.join();
        }
    }
}
