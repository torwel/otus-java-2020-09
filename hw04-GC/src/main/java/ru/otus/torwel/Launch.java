package ru.otus.torwel;

import com.sun.management.GarbageCollectionNotificationInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.management.*;
import javax.management.openmbean.CompositeData;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
//import java.util.logging.Logger;

public class Launch {

    private static final Logger logger = LogManager.getLogger();

    private static final BenchmarkMonitor listener = new BenchmarkMonitor();

    private static final String REPORT_MAX_DURARION_STW = "Maximum duration STW: ";
    private static final String REPORT_TOTAL_DURARION_STWS = "Total duration STWs: ";


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
            System.out.print(REPORT_MAX_DURARION_STW);
            System.out.println(listener.getMaxDurationSTW());

            System.out.print(REPORT_TOTAL_DURARION_STWS);
            System.out.println(listener.getTotalDurationSTW());
        }


        System.out.println("time:" + (System.currentTimeMillis() - beginTime) / 1000);
    }




    private static void stopGCMonitoring() {
        for (GarbageCollectorMXBean gcbean : ManagementFactory.getGarbageCollectorMXBeans()) {
            System.out.println("GC name:" + gcbean.getName());
            try {
                NotificationEmitter emitter = (NotificationEmitter) gcbean;
                emitter.removeNotificationListener(listener);
            } catch (ListenerNotFoundException e) {
            }
        }
    }

    private static void startGCMonitoring() {
        for (GarbageCollectorMXBean gcbean : ManagementFactory.getGarbageCollectorMXBeans()) {
            NotificationEmitter emitter = (NotificationEmitter) gcbean;
            emitter.addNotificationListener(listener, null, null);
        }
    }

    public static class BenchmarkMonitor implements NotificationListener {

        private long maxDurationSTW;
        private long totalDurationSTW;

        @Override
        public void handleNotification(Notification notification, Object handback) {
            if (notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
                GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());

                String gcName = info.getGcName();
                String gcAction = info.getGcAction();
                String gcCause = info.getGcCause();

                long startTime = info.getGcInfo().getStartTime();
                long duration = info.getGcInfo().getDuration();
                setMaxDurationSTW(duration);
                totalDurationSTW = totalDurationSTW + duration;

                logger.info("LOG. start:" + startTime + " Name:" + gcName + ", action:" + gcAction + ", gcCause:" + gcCause + "(" + duration + " ms)");
                System.out.println("SOUT. start:" + startTime + " Name:" + gcName + ", action:" + gcAction + ", gcCause:" + gcCause + "(" + duration + " ms)");
//                System.out.println("Maximum duration STW: " + listener.getMaxDurationSTW());
//                System.out.println("Total duration STWs: " + listener.getTotalDurationSTW());
            }
        }

        public long getMaxDurationSTW() {
            return maxDurationSTW;
        }

        public long getTotalDurationSTW() {
            return totalDurationSTW;
        }

        private void setMaxDurationSTW(long currentDuration) {
            if (currentDuration > maxDurationSTW)
                maxDurationSTW = currentDuration;
        };
    }
}
