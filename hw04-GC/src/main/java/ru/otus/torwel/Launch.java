package ru.otus.torwel;

import javax.management.*;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;

public class Launch {

    final private static BenchmarkMonitor listener = new BenchmarkMonitor();

    public static void main(String[] args) throws Exception {
        System.out.println("Starting pid: " + ManagementFactory.getRuntimeMXBean().getName());

        startGCMonitoring();

        long beginTime = System.currentTimeMillis();

        int size = 50_000;
        int loopCounter = 10000;
        //int loopCounter = 100000;
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("ru.otus:type=Benchmark");

        Benchmark mbean = new Benchmark(loopCounter);
        mbs.registerMBean(mbean, name);
        mbean.setSize(size);

        try {
            mbean.run();
        } finally {
//            System.out.println("Maximum duration STW: " + listener.getMaxDurationSTW());
//            System.out.println("Total duration STWs: " + listener.getTotalDurationSTW());
            System.out.println(listener.getMaxDurationSTW());
            System.out.println(listener.getTotalDurationSTW());
        }


        System.out.println("time:" + (System.currentTimeMillis() - beginTime) / 1000);


    }


    private static void startGCMonitoring() {
        for (GarbageCollectorMXBean gcbean : ManagementFactory.getGarbageCollectorMXBeans()) {
            System.out.println("GC name:" + gcbean.getName());
            try {
                NotificationEmitter emitter = (NotificationEmitter) gcbean;
                emitter.removeNotificationListener(listener);
            } catch (ListenerNotFoundException e) {
            }
        }
    }

    private static void stopGCMonitoring() {
        for (GarbageCollectorMXBean gcbean : ManagementFactory.getGarbageCollectorMXBeans()) {
            NotificationEmitter emitter = (NotificationEmitter) gcbean;
            emitter.addNotificationListener(listener, null, null);
        }
    }


}
