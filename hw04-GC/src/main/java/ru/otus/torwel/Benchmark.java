package ru.otus.torwel;

import java.util.ArrayList;
import java.util.List;

public class Benchmark implements BenchmarkMBean {
    private final int loopCounter;
    private volatile int size = 0;

    public Benchmark(int loopCounter) {
        this.loopCounter = loopCounter;
    }

    void run() throws InterruptedException {
        List<String> list = new ArrayList<>();

        for (int idx = 0; idx < loopCounter; idx++) {
            int local = size;

            for (int i = 0; i < local; i++) {
                list.add(new String(new char[0]));
            }

            for (int i = 0; i < local; i++) {
                if (1 == i % 2) list.remove(i);
            }

/*
            Object[] array = new Object[local];
            for (int i = 0; i < local; i++) {
                array[i] = new String(new char[0]);
            }
*/

            System.out.println("loop: " + idx);
            Thread.sleep(10); //Label_1
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public void setSize(int size) {
        System.out.println("new size:" + size);
        this.size = size;
    }
}
