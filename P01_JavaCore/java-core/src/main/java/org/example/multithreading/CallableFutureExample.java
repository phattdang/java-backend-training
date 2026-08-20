package org.example.multithreading;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CallableFutureExample {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService threadPool = Executors.newFixedThreadPool(2);

        Callable<Integer> calculateTotalPriceTask = () -> {
            System.out.println(Thread.currentThread().getName() + " starts calculating total price");
            sleep(2000);
            return 100 + 200 + 300;
        };

        Callable<String> loadUserNameTask = () -> {
            System.out.println(Thread.currentThread().getName() + " starts loading user name");
            sleep(1000);
            return "Phat";
        };

        Future<Integer> totalPriceFuture = threadPool.submit(calculateTotalPriceTask);
        Future<String> userNameFuture = threadPool.submit(loadUserNameTask);

        System.out.println("main-thread can do other work while tasks are running");

        String userName = userNameFuture.get();
        System.out.println("Loaded user name = " + userName);

        Integer totalPrice = totalPriceFuture.get();
        System.out.println("Calculated total price = " + totalPrice);

        threadPool.shutdown();
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
