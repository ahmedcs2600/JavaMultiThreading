package uberrideproblem;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/*
Imagine at the end of a political conference, republicans and democrats are trying to leave the venue and ordering Uber rides at the same time.
However, to make sure no fight breaks out in an Uber ride,
the software developers at Uber come up with an algorithm whereby either an Uber ride can have all democrats or republicans or two Democrats and two Republicans
All other combinations can result in a fist-fight.
 */
public class UberSeatingProblem {
    int democratCount = 0;
    int republicanCount = 0;

    ReentrantLock lock = new ReentrantLock();

    Semaphore democratWaiting = new Semaphore(4);
    Semaphore republicanWaiting = new Semaphore(4);

    CyclicBarrier barrier = new CyclicBarrier(4);

    public void seated() throws BrokenBarrierException, InterruptedException {
        System.out.println(Thread.currentThread().getName() + " seated");
    }

    public void drive() {
        System.out.println("Uber Ride on its way with ride leader " + Thread.currentThread().getName());
    }

    public void seatDemocrat() throws BrokenBarrierException, InterruptedException {
        boolean rideLeader = false;
        lock.lock();
        democratCount++;
        if(democratCount == 4) {
            democratWaiting.release(3);
            democratCount -= 4;
            rideLeader = true;
        } else if(democratCount == 2 && republicanCount >= 2) {
            democratWaiting.release(1);
            republicanWaiting.release(2);
            republicanCount -= 2;
            democratCount -= 2;
            rideLeader = true;
        } else {
            lock.unlock();
            democratWaiting.acquire();
        }
        seated();
        barrier.await();
        if(rideLeader) {
            drive();
            lock.unlock();
        }
    }

    public void seatRepublican() throws BrokenBarrierException, InterruptedException {
        boolean rideLeader = false;
        lock.lock();
        republicanCount++;
        if(republicanCount == 4) {
            republicanWaiting.release(3);
            republicanCount -= 4;
            rideLeader = true;
        } else if(republicanCount == 2 && democratCount >= 2) {
            republicanWaiting.release(1);
            democratWaiting.release(2);
            republicanCount -= 2;
            democratCount -= 2;
            rideLeader = true;
        } else {
            lock.unlock();
            republicanWaiting.acquire();
        }
        seated();
        barrier.await();
        if(rideLeader) {
            drive();
            lock.unlock();
        }
    }
}
