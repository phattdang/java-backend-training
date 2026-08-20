package org.example.multithreading;

public class SynchronizedLockExample {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("1. synchronized method locks this");
        MethodLockCounter methodLockCounter = new MethodLockCounter();
        runTwoThreads(methodLockCounter::increment);
        System.out.println("count = " + methodLockCounter.count);

        System.out.println("\n2. synchronized (this) locks this, but only inside the block");
        ThisBlockCounter thisBlockCounter = new ThisBlockCounter();
        runTwoThreads(thisBlockCounter::increment);
        System.out.println("count = " + thisBlockCounter.count);

        System.out.println("\n3. synchronized (lockObject) locks the chosen lock object");
        PrivateLockCounter privateLockCounter = new PrivateLockCounter();
        runTwoThreads(privateLockCounter::increment);
        System.out.println("count = " + privateLockCounter.count);

        System.out.println("\n4. Different objects have different locks");
        MethodLockCounter counter1 = new MethodLockCounter();
        MethodLockCounter counter2 = new MethodLockCounter();

        Thread t1 = new Thread(counter1::slowIncrement, "worker-1");
        Thread t2 = new Thread(counter2::slowIncrement, "worker-2");

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("counter1.count = " + counter1.count);
        System.out.println("counter2.count = " + counter2.count);
    }

    private static void runTwoThreads(Runnable task) throws InterruptedException {
        Thread t1 = new Thread(task, "worker-1");
        Thread t2 = new Thread(task, "worker-2");

        t1.start();
        t2.start();

        t1.join();
        t2.join();
    }

    static class MethodLockCounter {
        int count = 0;

        synchronized void increment() {
            count++;
        }

        synchronized void slowIncrement() {
            System.out.println(Thread.currentThread().getName() + " acquired lock of " + this);
            sleep(1000);
            count++;
            System.out.println(Thread.currentThread().getName() + " released lock of " + this);
        }
    }

    static class ThisBlockCounter {
        int count = 0;

        void increment() {
            System.out.println(Thread.currentThread().getName() + " can run code before synchronized block");

            synchronized (this) {
                count++;
            }

            System.out.println(Thread.currentThread().getName() + " can run code after synchronized block");
        }
    }

    static class PrivateLockCounter {
        private final Object countLock = new Object();
        int count = 0;

        void increment() {
            synchronized (countLock) {
                count++;
            }
        }
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println(Thread.currentThread().getName() + " was interrupted");
        }
    }
}
