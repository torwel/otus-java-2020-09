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

            System.out.println("Creating objects...");
            for (int i = 0; i < local; i++) {
                list.add(new String(new char[0]));
//                if (i % 1000 == 0)
//                    System.out.println("add " + i);
            }

            System.out.println("Removing objects...");
            var lstSize = list.size();
            for (int i = lstSize - 1; i >= lstSize - local/4*3; i--) {
                list.remove(i);
//                if (i % 1000 == 0)
//                    System.out.println("remove " + i);
            }

/*
            Object[] array = new Object[local];
            for (int i = 0; i < local; i++) {
                array[i] = new String(new char[0]);
            }
*/

            System.out.println("loop: " + idx + "\tlstSize: " + lstSize);
            System.out.println("Maximum duration STW: " + Launch.listener.getMaxDurationSTW());
            System.out.println("Total duration STWs: " + Launch.listener.getTotalDurationSTW());
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
