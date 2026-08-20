package org.example.multithreading;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntegerExample {
    private static final int INCREMENT_TIMES = 100_000;

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Unsafe count++");
        UnsafeCounter unsafeCounter = new UnsafeCounter();
        runTwoThreads(unsafeCounter::increment);

        System.out.println("Expected: " + (INCREMENT_TIMES * 2));
        System.out.println("Actual:   " + unsafeCounter.count);

        System.out.println("\nAtomicInteger incrementAndGet()");
        AtomicCounter atomicCounter = new AtomicCounter();
        runTwoThreads(atomicCounter::increment);

        System.out.println("Expected: " + (INCREMENT_TIMES * 2));
        System.out.println("Actual:   " + atomicCounter.getCount());

        System.out.println("\nAtomicInteger as sequence generator");
        SequenceGenerator sequenceGenerator = new SequenceGenerator();

        Thread t1 = new Thread(() -> printNextIds(sequenceGenerator), "worker-1");
        Thread t2 = new Thread(() -> printNextIds(sequenceGenerator), "worker-2");

        t1.start();
        t2.start();

        t1.join();
        t2.join();
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

    private static void printNextIds(SequenceGenerator sequenceGenerator) {
        for (int i = 0; i < 5; i++) {
            System.out.printf(
                    "%s generated id = %d%n",
                    Thread.currentThread().getName(),
                    sequenceGenerator.nextId()
            );
        }
    }

    static class UnsafeCounter {
        int count = 0;

        void increment() {
            count++;
        }
    }

    static class AtomicCounter {
        private final AtomicInteger count = new AtomicInteger(0);

        void increment() {
            count.incrementAndGet();
        }

        int getCount() {
            return count.get();
        }
    }

    static class SequenceGenerator {
        private final AtomicInteger nextId = new AtomicInteger(0);

        int nextId() {
            return nextId.incrementAndGet();
        }
    }
}
