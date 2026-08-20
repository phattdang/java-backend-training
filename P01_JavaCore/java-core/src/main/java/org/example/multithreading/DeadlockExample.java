package org.example.multithreading;

public class DeadlockExample {
    public static void main(String[] args) throws InterruptedException {
        InventoryService inventoryService = new InventoryService();
        OrderService orderService = new OrderService();

        inventoryService.setOrderService(orderService);
        orderService.setInventoryService(inventoryService);

        Thread orderThread = new Thread(orderService::placeOrder, "order-thread");
        Thread inventoryThread = new Thread(inventoryService::refreshStock, "inventory-thread");

        orderThread.start();
        inventoryThread.start();

        Thread.sleep(3000);

        printState(orderThread);
        printState(inventoryThread);
        System.out.println("If both threads are BLOCKED, they are waiting for each other's lock.");
    }

    static class OrderService {
        private InventoryService inventoryService;

        void setInventoryService(InventoryService inventoryService) {
            this.inventoryService = inventoryService;
        }

        synchronized void placeOrder() {
            System.out.println("order-thread locked OrderService");
            sleep(500);

            System.out.println("order-thread needs InventoryService");
            inventoryService.reserveStock();
        }

        synchronized void updateOrderStatus() {
            System.out.println("inventory-thread locked OrderService");
        }
    }

    static class InventoryService {
        private OrderService orderService;

        void setOrderService(OrderService orderService) {
            this.orderService = orderService;
        }

        synchronized void refreshStock() {
            System.out.println("inventory-thread locked InventoryService");
            sleep(500);

            System.out.println("inventory-thread needs OrderService");
            orderService.updateOrderStatus();
        }

        synchronized void reserveStock() {
            System.out.println("order-thread locked InventoryService");
        }
    }

    private static void printState(Thread thread) {
        System.out.printf("%s state = %s%n", thread.getName(), thread.getState());
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
