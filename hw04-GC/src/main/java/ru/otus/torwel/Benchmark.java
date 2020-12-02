package ru.otus.torwel;

import java.util.LinkedList;
import java.util.List;

public class Benchmark implements BenchmarkMBean {
    private final int loopCounter;
    private volatile int size = 0;

    public Benchmark(int loopCounter) {
        this.loopCounter = loopCounter;
    }

    void run() throws InterruptedException {
        List<String[]> list = new LinkedList<>();

        for (int idx = 0; idx < loopCounter; idx++) {
            int local = size;
            // Определяем размер приращения списка массивов
            int incrementCapacity = local / 10;
            for (int i = 0; i < incrementCapacity; i++) {
                String[] arr = new String[local];
                for (int j = 0; j < local; j++) {
                    arr[j] = new String("This is the benchmark. Fill your memory with this string.");
                }
                list.add(arr);
            }

            // Необходимо удалить примерно 1/4 от количества добавленных в список массивов.
            // Определяем шаг прохода по списку.
            int step = (int) (list.size() / ( incrementCapacity / 4.0));

            int countDels = 0;
            // используя полученный шаг, удаляем элементы списка
            for (int i = 0; i < list.size(); i += step) {
                list.remove(i);
                countDels++;
            }
            System.out.println("loop: " + idx + "\tlstSize: " + list.size() + "\tdeleted elements: " + countDels);
            Thread.sleep(5); //Label_1: use for 256M test
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
