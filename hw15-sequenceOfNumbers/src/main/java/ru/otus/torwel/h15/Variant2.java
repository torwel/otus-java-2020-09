package ru.otus.torwel.h15;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Variant2 {

    private final Lock lock = new ReentrantLock();
    private volatile int index = 0;

    public static void main(String[] args) {
        new Variant2().startApp();
    }

    private void startApp() {

        int[] seq = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 9, 8, 7, 6, 5, 4, 3, 2};

        Thread t1 = new Thread(() -> process(seq));
        t1.setName("1   ");

        Thread t2 = new Thread(() -> process(seq));
        t2.setName("-      ");

        t1.start();
        t2.start();

        sleep(24_000);
        sleep(24);
        t1.interrupt();
        t2.interrupt();

    }

    private void process(int[] sequence) {
        int notMyIndex = -1;

        while (!Thread.currentThread().isInterrupted()) {
            lock.lock();
            try {
                if (notMyIndex != index) {
                    System.out.printf("%s %d\n", Thread.currentThread().getName(), sequence[index]);
                    notMyIndex = index + 1;
                    if (notMyIndex == sequence.length) {
                        notMyIndex = 0;
                    }
                    index = notMyIndex;
                }
            } finally {
                lock.unlock();
            }
            sleep(500);
        }
    }

    private static void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
