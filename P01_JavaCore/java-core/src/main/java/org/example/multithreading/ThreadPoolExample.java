package org.example.multithreading;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadPoolExample {
    public static void main(String[] args) throws InterruptedException {
        int numberOfWorkers = 3;
        int numberOfTasks = 10;

        ExecutorService threadPool = Executors.newFixedThreadPool(numberOfWorkers);

        System.out.println("Thread pool has " + numberOfWorkers + " worker threads");
        System.out.println("Submit " + numberOfTasks + " tasks");
        System.out.println("Only " + numberOfWorkers + " tasks can run at the same time\n");

        for (int taskId = 1; taskId <= numberOfTasks; taskId++) {
            int currentTaskId = taskId;

            System.out.println("main-thread submits task-" + currentTaskId);

            threadPool.submit(() -> {
                String workerName = Thread.currentThread().getName();

                System.out.println(workerName + " starts task-" + currentTaskId);
                sleep(1000);
                System.out.println(workerName + " finishes task-" + currentTaskId);
            });
        }

        threadPool.shutdown();
        boolean finished = threadPool.awaitTermination(10, TimeUnit.SECONDS);

        System.out.println("\nAll tasks finished = " + finished);
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
