package barbershop;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class BarberShopProblem {
    private final int waitCount;
    private int waitingCustomers;
    private final Semaphore waitForCustomerToEnter;
    private final Semaphore waitForBarberToGetReady;
    private final Semaphore waitForBarberToCutHair;
    private final Semaphore waitForCustomerToLeave;
    private final ReentrantLock lock;

    public BarberShopProblem(int waitCount) {
        this.waitCount = waitCount;
        this.waitingCustomers = 0;
        waitForCustomerToEnter = new Semaphore(0);
        waitForBarberToGetReady = new Semaphore(0);
        waitForBarberToCutHair = new Semaphore(0);
        waitForCustomerToLeave = new Semaphore(0);
        lock = new ReentrantLock();
    }

    public void customerWalksIn() throws InterruptedException {
        lock.lock();
        if(waitingCustomers == waitCount) {
            lock.unlock();
            return;
        }
        waitingCustomers++;
        lock.unlock();

        waitForCustomerToEnter.release();
        waitForBarberToGetReady.acquire();

        waitForBarberToCutHair.acquire();
        waitForCustomerToLeave.release();

        lock.lock();
        waitingCustomers--;
        lock.unlock();
    }

    private void barber() throws InterruptedException {
        while(true) {
            waitForCustomerToEnter.acquire();
            waitForBarberToGetReady.release();

            Thread.sleep(100);

            waitForBarberToCutHair.release();
            waitForCustomerToLeave.acquire();
        }
    }

    public static void main(String[] args) {

    }
}
