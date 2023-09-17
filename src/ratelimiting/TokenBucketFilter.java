package ratelimiting;

import java.util.HashSet;
import java.util.Set;

public class TokenBucketFilter {
    private final int maxTokens;
    private long lastRequest = System.currentTimeMillis();
    private long remain = 0;

    public TokenBucketFilter(int maxTokens) {
        this.maxTokens = maxTokens;
    }

    public synchronized void getToken() throws InterruptedException {
        remain += (System.currentTimeMillis() - lastRequest) / 1000;
        remain = Math.min(remain, maxTokens);
        if(remain == 0) {
            Thread.sleep(1000L);
        } else {
            remain--;
        }
        lastRequest = System.currentTimeMillis();
        System.out.println("Granting " + Thread.currentThread().getName() + "token at " + System.currentTimeMillis() / 1000);
    }

    public static void main(String[] args) throws InterruptedException {
        Set<Thread> allThreads = new HashSet<>();
        final TokenBucketFilter tokenBucketFilter = new TokenBucketFilter(5);

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
