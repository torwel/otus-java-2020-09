package ru.otus.torwel;

import com.sun.management.GarbageCollectionNotificationInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.*;
import javax.management.openmbean.CompositeData;
import java.lang.management.*;
import java.text.SimpleDateFormat;
import java.util.*;
//import java.util.logging.Logger;

public class Launch {

    private static final Logger logger = LoggerFactory.getLogger(Launch.class);

    private static final BenchmarkMonitor listener = new BenchmarkMonitor();

    private static final String REPORT_MAX_DURARION_STW = "Maximum duration STW: ";
    private static final String REPORT_TOTAL_DURARION_STWS = "Total duration STWs: ";


    public static void main(String[] args) throws Exception {
        System.out.println("Starting pid: " + ManagementFactory.getRuntimeMXBean().getName());

        long beginTime = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
        logger.info("application starts at {}", sdf.format(beginTime));

        startGCMonitoring();

        int size = 50_000;
        int loopCounter = 20000;
        //int loopCounter = 100000;
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("ru.otus:type=Benchmark");

        Benchmark mbean = new Benchmark(loopCounter);
        mbs.registerMBean(mbean, name);
        mbean.setSize(size);

        try {
            mbean.run();
        } finally {
//            logger.debug(REPORT_MAX_DURARION_STW + listener.getMaxDurationSTW());
//            logger.debug(REPORT_TOTAL_DURARION_STWS + listener.getTotalDurationSTW());
            logger.debug("work time: {} millis", (System.currentTimeMillis() - beginTime));
        }

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


    // TODO: Добавить usedMemory Before, usedMemory After, commitedMemory Before, commitedMemory After
    // TODO: Возможно добавить изменение обоих типов памяти
    private static void startGCMonitoring() {
        logger.info("GC start time;GC duration;" +
                "usedMemory Before;usedMemory After;" +
                "commitedMemory Before;commitedMemory After;" +
                "gcCause;gcName;gcAction");
        for (GarbageCollectorMXBean gcbean : ManagementFactory.getGarbageCollectorMXBeans()) {
            NotificationEmitter emitter = (NotificationEmitter) gcbean;
            emitter.addNotificationListener(listener, null, null);
        }
    }

    public static class BenchmarkMonitor implements NotificationListener {

//        private volatile long maxDurationSTW;
//        private volatile long totalDurationSTW;

        @Override
        public void handleNotification(Notification notification, Object handback) {
            if (notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
                GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());

                String gcName = info.getGcName();
                String gcAction = info.getGcAction();
                String gcCause = info.getGcCause();

                long startTime = info.getGcInfo().getStartTime();
                long duration = info.getGcInfo().getDuration();
//                setMaxDurationSTW(duration);
//                totalDurationSTW += duration;

                long usedMemoryBefore = 0;
                long usedMemoryAfter = 0;
                long commitedMemoryBefore = 0;
                long commitedMemoryAfter = 0;

                List<String> listHeapMemTypes = new ArrayList<>();
                for(MemoryPoolMXBean mBean: ManagementFactory.getMemoryPoolMXBeans()) {
                    //TODO: Здесь составляем список mem с типами памяти MemoryType.HEAP
                    if (mBean.getType() == MemoryType.HEAP) {
                        listHeapMemTypes.add(mBean.getName());
//                        System.out.println(mBean.getName() + " - " + mBean.getType());
                    }
                }

                // TODO: Далее для mapBefore и mapAfter посчитать сумму памяти HEAP типов
                // и вывести в лог в нужном формате

                Map<String, MemoryUsage> mapBefore = info.getGcInfo().getMemoryUsageBeforeGc();
                for (Map.Entry<String, MemoryUsage> entry : mapBefore.entrySet()) {
                    String memTypeName = entry.getKey();
                    if (!listHeapMemTypes.contains(memTypeName))
                        continue;
                    MemoryUsage memoryUsage = mapBefore.get(memTypeName);
                    usedMemoryBefore += memoryUsage.getUsed();
                    commitedMemoryBefore += memoryUsage.getCommitted();

//                    System.out.print("Memory type: " + memTypeName);
//                    System.out.print(" Initial Size: " + memoryUsage.getInit());
//                    System.out.print(" Used: " + memoryUsage.getUsed());
//                    System.out.print(" Max: " + memoryUsage.getMax());
//                    System.out.print(" Committed: " + memoryUsage.getCommitted());
//                    System.out.println();
                }

                Map<String, MemoryUsage> mapAfter = info.getGcInfo().getMemoryUsageAfterGc();
                for (Map.Entry<String, MemoryUsage> entry : mapAfter.entrySet()) {
                    String memTypeName = entry.getKey();
                    if (!listHeapMemTypes.contains(memTypeName))
                        continue;
                    MemoryUsage memoryUsage = mapAfter.get(memTypeName);
                    usedMemoryAfter += memoryUsage.getUsed();
                    commitedMemoryAfter += memoryUsage.getCommitted();

//                    System.out.print("Memory type: " + memTypeName);
//                    System.out.print(" Initial Size: " + memoryUsage.getInit());
//                    System.out.print(" Used: " + memoryUsage.getUsed());
//                    System.out.print(" Max: " + memoryUsage.getMax());
//                    System.out.print(" Committed: " + memoryUsage.getCommitted());
//                    System.out.println();
                }

                logger.info("{};{};{};{};{};{};{};{};{}",
                        startTime,
                        duration,
                        usedMemoryBefore  / 1024 / 1024,
                        usedMemoryAfter / 1024 / 1024,
                        commitedMemoryBefore / 1024 / 1024,
                        commitedMemoryAfter / 1024 / 1024,
                        gcCause,
                        gcName,
                        gcAction);
                logger.debug("start:" + startTime +
                        " Name:" + gcName +
                        ", action:" + gcAction +
                        ", gcCause:" + gcCause +
                        "(" + duration + " ms)");
            }
        }

//        public long getMaxDurationSTW() {
//            return maxDurationSTW;
//        }

//        public long getTotalDurationSTW() {
//            return totalDurationSTW;
//        }

//        private void setMaxDurationSTW(long currentDuration) {
//            if (currentDuration > maxDurationSTW)
//                maxDurationSTW = currentDuration;
//        };
    }
}
