package ru.otus.torwel.h15;

public class Variant1 {

    private static final int COUNTER_MIN = 1;
    private static final int COUNTER_MAX = 10;

    private static volatile int counter = 0;
    private static volatile int step = 1;


    public static void main(String[] args) {
        new Variant1().startApp();
    }

    private void startApp(){
        Thread t1 = new Thread(this::process);
        t1.setName("1   ");

        Thread t2 = new Thread(this::process);
        t2.setName("-      ");

        t1.start();
        t2.start();

        sleep(24_000);
        sleep(24);
        t1.interrupt();
        t2.interrupt();
    }

    private void process() {
        int copyCounter = -1;
        while (!Thread.currentThread().isInterrupted()) {
            try {
                synchronized (this) {
                    while (counter == copyCounter) {
                        wait();
                    }

                    counter = counter + step;
                    System.out.printf("%s %d\n", Thread.currentThread().getName(), counter);
                    copyCounter = counter;

                    if (copyCounter == COUNTER_MAX) {
                        step = -1;
                    }
                    if (copyCounter == COUNTER_MIN) {
                        step = 1;
                    }

                    notifyAll();
                    sleep(500);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
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
