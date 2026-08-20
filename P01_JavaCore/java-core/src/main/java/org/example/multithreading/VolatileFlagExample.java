package org.example.multithreading;

public class VolatileFlagExample {
    public static void main(String[] args) throws InterruptedException {
        Worker worker = new Worker();

        Thread workerThread = new Thread(worker::run, "worker-thread");
        workerThread.start();

        Thread.sleep(1000);

        System.out.println("main-thread asks worker-thread to stop");
        worker.stop();

        workerThread.join();
        System.out.println("main-thread finished");
    }

    static class Worker {
        private volatile boolean running = true;

        void run() {
            long workCount = 0;

            while (running) {
                workCount++;
            }

            System.out.println(Thread.currentThread().getName() + " stopped after workCount = " + workCount);
        }

        void stop() {
            running = false;
        }
    }
}
