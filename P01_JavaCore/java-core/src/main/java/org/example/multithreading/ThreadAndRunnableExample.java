package org.example.multithreading;

public class ThreadAndRunnableExample {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Main thread: " + Thread.currentThread().getName());

        System.out.println("\n1. extends Thread");
        Thread countingThread = new CountingThread("extends-thread");
        countingThread.start();

        System.out.println("\n2. Runnable");
        Runnable task = () -> printNumbers("runnable-task");

        Thread worker1 = new Thread(task, "worker-1");
        Thread worker2 = new Thread(task, "worker-2");

        worker1.start();
        worker2.start();

        countingThread.join();
        worker1.join();
        worker2.join();

        System.out.println("\n3. Calling run() directly");
        Thread directRunThread = new CountingThread("direct-run-thread");
        directRunThread.run();

        System.out.println("\nDone on: " + Thread.currentThread().getName());
    }

    static class CountingThread extends Thread {
        CountingThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            printNumbers("extends Thread");
        }
    }

    private static void printNumbers(String label) {
        for (int i = 1; i <= 5; i++) {
            System.out.printf(
                    "%s | current thread = %s | i = %d%n",
                    label,
                    Thread.currentThread().getName(),
                    i
            );
        }
    }
}
