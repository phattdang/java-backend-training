package org.example.multithreading;

public class ThreadStateExample {
    public static void main(String[] args) throws InterruptedException {
        Thread sleepingThread = new Thread(() -> {
            printCurrentThread("started");

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("sleeping-thread was interrupted");
            }

            printCurrentThread("finished");
        }, "sleeping-thread");

        printState("After creating thread", sleepingThread);

        sleepingThread.start();
        printState("Right after start", sleepingThread);

        Thread.sleep(500);
        printState("While thread is sleeping", sleepingThread);

        sleepingThread.join();
        printState("After join completes", sleepingThread);

        System.out.println("\nWAITING example with join()");

        Thread longRunningThread = new Thread(() -> sleep(3000), "long-running-thread");
        Thread joiningThread = new Thread(() -> {
            try {
                longRunningThread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("joining-thread was interrupted");
            }
        }, "joining-thread");

        longRunningThread.start();
        joiningThread.start();

        Thread.sleep(500);
        printState("Thread waiting for another thread to finish", joiningThread);

        longRunningThread.join();
        joiningThread.join();
        printState("Joining thread after work is done", joiningThread);
    }

    private static void printState(String label, Thread thread) {
        System.out.printf("%-45s | %-20s | %s%n", label, thread.getName(), thread.getState());
    }

    private static void printCurrentThread(String action) {
        System.out.printf("%-45s | %-20s%n", action, Thread.currentThread().getName());
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
