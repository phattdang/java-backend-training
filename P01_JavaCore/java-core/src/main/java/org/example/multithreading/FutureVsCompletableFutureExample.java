package org.example.multithreading;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FutureVsCompletableFutureExample {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("1. Future: main-thread manually waits and calls the next step");
        futureExample();

        System.out.println("\n2. CompletableFuture: describe the async pipeline");
        completableFutureExample();
    }

    private static void futureExample() throws ExecutionException, InterruptedException {
        ExecutorService threadPool = Executors.newFixedThreadPool(2);

        Future<User> userFuture = threadPool.submit(() -> loadUser(1));

        User user = userFuture.get();
        String email = buildEmail(user);
        sendEmail(email);

        threadPool.shutdown();
    }

    private static void completableFutureExample() {
        CompletableFuture<Void> pipeline = CompletableFuture
                .supplyAsync(() -> loadUser(2))
                .thenApply(FutureVsCompletableFutureExample::buildEmail)
                .thenAccept(FutureVsCompletableFutureExample::sendEmail)
                .exceptionally(error -> {
                    System.out.println("Failed to process pipeline: " + error.getMessage());
                    return null;
                });

        System.out.println("main-thread can continue after creating the pipeline");

        pipeline.join();
    }

    private static User loadUser(int userId) {
        System.out.println(Thread.currentThread().getName() + " loads user-" + userId);
        sleep(1000);
        return new User(userId, "phat" + userId + "@example.com");
    }

    private static String buildEmail(User user) {
        System.out.println(Thread.currentThread().getName() + " builds email for user-" + user.id);
        sleep(500);
        return "Hello " + user.email;
    }

    private static void sendEmail(String email) {
        System.out.println(Thread.currentThread().getName() + " sends email: " + email);
        sleep(500);
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println(Thread.currentThread().getName() + " was interrupted");
        }
    }

    record User(int id, String email) {
    }
}
