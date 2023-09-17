package unisexbathroomproblem;

import java.util.HashSet;
import java.util.Set;

public class UnixBathroom {

    private int useBy = -1; // Male(1), Female(2)
    private int cnt = 0;

    public void maleUseBathroom(String name) throws InterruptedException {
        synchronized (this) {
            while (useBy == 2 || cnt >= 3) {
                this.wait();
            }
            cnt++;
            useBy = 1;
        }

        //Use Bathroom
        useBathroom(name);

        synchronized (this) {
            cnt--;
            if (cnt == 0) {
                useBy = -1;
                this.notifyAll();
            }
        }
    }

    private void useBathroom(String name) throws InterruptedException {
        System.out.println(name + " is using the bathroom and gender is " + ((useBy == 1) ? "MALE" : "FEMALE"));
        Thread.sleep(1000L);
        System.out.println(name + " has done using the bathroom and gender is " + ((useBy == 1) ? "MALE" : "FEMALE"));
    }

    public void femaleUseBathroom(String name) throws InterruptedException {
        synchronized (this) {
            while (useBy == 1 || cnt >= 3) {
                this.wait();
            }
            cnt++;
            useBy = 2;
        }

        //Use Bathroom
        useBathroom(name);

        synchronized (this) {
            cnt--;
            if (cnt == 0) {
                useBy = -1;
                this.notifyAll();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        UnixBathroom unixBathroom = new UnixBathroom();
        Set<Thread> allThreads = new HashSet<>();
        for(int i = 0; i < 10; i++) {
            if(i % 2 == 0) {
                int finalI = i;
                Thread f = new Thread(() -> {
                    try {
                        unixBathroom.femaleUseBathroom("FEMALE -> " + finalI);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
                allThreads.add(f);
            }

            if(i % 2 == 0) {
                int finalI = i;
                Thread m = new Thread(() -> {
                    try {
                        unixBathroom.maleUseBathroom("MALE -> " + finalI);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
                allThreads.add(m);
            }
        }

        for(Thread t : allThreads) {
            t.start();
        }

        for(Thread t : allThreads) {
            t.join();
        }
    }
}
