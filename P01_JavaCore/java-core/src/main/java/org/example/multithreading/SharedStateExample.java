package org.example.multithreading;

public class SharedStateExample {
    private static final int INCREMENT_TIMES = 100_000;

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Race condition example");
        UnsafeCounter unsafeCounter = new UnsafeCounter();
        runTwoThreads(unsafeCounter::increment);

        System.out.println("Expected: " + (INCREMENT_TIMES * 2));
        System.out.println("Actual:   " + unsafeCounter.count);

        System.out.println("\nFixed with synchronized");
        SafeCounter safeCounter = new SafeCounter();
        runTwoThreads(safeCounter::increment);

        System.out.println("Expected: " + (INCREMENT_TIMES * 2));
        System.out.println("Actual:   " + safeCounter.count);
    }

    private static void runTwoThreads(Runnable task) throws InterruptedException {
        Thread t1 = new Thread(() -> repeat(task), "worker-1");
        Thread t2 = new Thread(() -> repeat(task), "worker-2");

        t1.start();
        t2.start();

        t1.join();
        t2.join();
    }

    private static void repeat(Runnable task) {
        for (int i = 0; i < INCREMENT_TIMES; i++) {
            task.run();
        }
    }

    static class UnsafeCounter {
        int count = 0;

        void increment() {
            count++;
        }
    }

    static class SafeCounter {
        int count = 0;

        synchronized void increment() {
            count++;
        }
    }
}
