package ru.otus.torwel.h15;

public class SequenceOfNumber {

    private static final int COUNTER_MIN = 1;
    private static final int COUNTER_MAX = 10;

    private static volatile int counter;
    private static volatile int direction;


    public static void main(String[] args) {
        new SequenceOfNumber().startApp1();
    }

    private void startApp1(){
        direction = 1;

        Thread t1 = new Thread(this::step);
        t1.setName("1   ");

        Thread t2 = new Thread(this::step);
        t2.setName("-      ");

        t2.start();
        t1.start();

        sleep(24_000);
//        sleep(100);
        t1.interrupt();
        t2.interrupt();
    }

    private void step() {
        int copyCounter = -1;
        while (!Thread.currentThread().isInterrupted()) {
            try {
                synchronized (this) {
                    while (counter == copyCounter) {
                        this.wait();
                    }

                    counter = counter + direction;
                    System.out.printf("%s %d\n", Thread.currentThread().getName(), counter);
                    copyCounter = counter;

                    if (counter == COUNTER_MAX) {
                        direction = -1;
                    }
                    if (counter == COUNTER_MIN) {
                        direction = 1;
                    }
                    sleep(500);
                    notifyAll();
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
