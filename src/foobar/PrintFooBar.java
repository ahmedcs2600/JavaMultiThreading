package foobar;

/*
Suppose there are two threads t1 and t2. t1 prints Foo and t2 prints Bar.
You are required to write a program which takes a user input n.
Then the two threads print Foo and Bar alternately n number of times.
The code for the class it as follows

public class PrintFooBar {

    private final int n;

    PrintFooBar(int n) {
        this.n = n;
    }

    public void printFoo() {
        for(int i = 1; i <= n; i++) {
            System.out.println("Foo");
        }
    }

    public void printBar() {
        for(int i = 1; i <= n; i++) {
            System.out.println("Bar");
        }
    }
}
 */
public class PrintFooBar {

    private final int n;
    private int flag = 0;

    PrintFooBar(int n) {
        this.n = n;
    }

    public void printFoo() {
        for(int i = 1; i <= n; i++) {
            synchronized (this) {
                while (flag == 1) {
                    try { this.wait(); }
                    catch (Exception e) { }
                }
                System.out.println("Foo");
                flag = 1;
                this.notifyAll();
            }
        }
    }

    public void printBar() {
        for(int i = 1; i <= n; i++) {
            synchronized (this) {
                while (flag == 0) {
                    try { this.wait(); }
                    catch (Exception e) { }
                }
                System.out.println("Bar");
                flag = 0;
                this.notifyAll();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        PrintFooBar printFooBar = new PrintFooBar(10);
        FooBarThread thread1 = new FooBarThread(printFooBar, "foo");
        FooBarThread thread2 = new FooBarThread(printFooBar, "bar");

        thread2.start();
        thread1.start();

        thread1.join();
        thread2.join();
    }

    static class FooBarThread extends Thread {
        PrintFooBar fooBar;
        String method;

        public FooBarThread(PrintFooBar fooBar, String method) {
            this.fooBar = fooBar;
            this.method = method;
        }

        @Override
        public void run() {
            if(method.equals("foo")) {
                fooBar.printFoo();
            } else {
                fooBar.printBar();
            }
        }
    }
}
