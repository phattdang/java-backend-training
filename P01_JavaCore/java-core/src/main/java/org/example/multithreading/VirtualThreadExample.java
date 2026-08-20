package org.example.multithreading;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class VirtualThreadExample {
    private static final int NUMBER_OF_TASKS = 20;

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Same blocking tasks, two different executors\n");

        runWithFixedPlatformThreadPool();
        System.out.println();
        runWithVirtualThreadPerTaskExecutor();
    }

    private static void runWithFixedPlatformThreadPool() throws InterruptedException {
        int numberOfPlatformThreads = 3;

        System.out.println("1. Fixed platform thread pool");
        System.out.println("Only " + numberOfPlatformThreads + " tasks can run at the same time");

        ExecutorService executor = Executors.newFixedThreadPool(numberOfPlatformThreads);
        runTasks(executor);
    }

    private static void runWithVirtualThreadPerTaskExecutor() throws InterruptedException {
        System.out.println("2. Virtual thread per task executor");
        System.out.println("Each task gets its own virtual thread");

        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
        runTasks(executor);
    }

    private static void runTasks(ExecutorService executor) throws InterruptedException {
        Instant start = Instant.now();

        for (int taskId = 1; taskId <= NUMBER_OF_TASKS; taskId++) {
            int currentTaskId = taskId;

            executor.submit(() -> simulateBlockingIo(currentTaskId));
        }

        executor.shutdown();
        executor.awaitTermination(30, TimeUnit.SECONDS);

        long millis = Duration.between(start, Instant.now()).toMillis();
        System.out.println("Finished " + NUMBER_OF_TASKS + " tasks in about " + millis + " ms");
    }

    private static void simulateBlockingIo(int taskId) {
        Thread thread = Thread.currentThread();

        System.out.printf(
                "task-%02d started  | thread=%s | virtual=%s%n",
                taskId,
                thread.getName(),
                thread.isVirtual()
        );

        sleep(1000);

        System.out.printf(
                "task-%02d finished | thread=%s | virtual=%s%n",
                taskId,
                thread.getName(),
                thread.isVirtual()
        );
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
