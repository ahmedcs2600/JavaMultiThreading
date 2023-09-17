package dinningphilosophers;

/*
Imagine you have five philosopher's sitting on a roundtable. The philosopher's do only two kinds of activities.
One they contemplate and two they eat. However, they have only five forks between themselves to eat their food with. Each philosopher requires both the fork to his left and the fork to his right to eat his food.
 */

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Semaphore;

public class DinningPhilosophers2 {
    private Semaphore[] forks;

    DinningPhilosophers2() {
        forks = new Semaphore[5];
        for(int i = 0; i < 5; i++) {
            forks[i] = new Semaphore(1);
        }
    }

    public void eat(int id) throws InterruptedException {
        if(id == 3) {
            forks[(id + 1) % 5].acquire();
            forks[id].acquire();
        } else {
            forks[id].acquire();
            forks[(id + 1) % 5].acquire();
        }

        System.out.println(Thread.currentThread().getName() + " is eating");

        forks[id].release();
        forks[(id + 1) % 5].release();
    }

    public static void main(String[] args) throws InterruptedException {
        Set<Thread> allThreads = new HashSet<>();
        DinningPhilosophers2 dinningPhilosophers = new DinningPhilosophers2();

        Thread t1 = new Thread(() -> {
            try {
                dinningPhilosophers.eat(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        allThreads.add(t1);

        Thread t2 = new Thread(() -> {
            try {
                dinningPhilosophers.eat(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        allThreads.add(t2);

        Thread t3 = new Thread(() -> {
            try {
                dinningPhilosophers.eat(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        allThreads.add(t3);

        Thread t4 = new Thread(() -> {
            try {
                dinningPhilosophers.eat(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        allThreads.add(t4);

        Thread t5 = new Thread(() -> {
            try {
                dinningPhilosophers.eat(4);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        allThreads.add(t5);

        for(Thread thread : allThreads) {
            thread.start();
        }

        for(Thread thread : allThreads) {
            thread.join();
        }
    }
}
