package ru.otus.torwel;

import com.sun.management.GarbageCollectionNotificationInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.*;
import javax.management.openmbean.CompositeData;
import java.lang.management.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Launch {

    private static final Logger logger = LoggerFactory.getLogger(Launch.class);

    private static final BenchmarkMonitor listener = new BenchmarkMonitor();

    public static void main(String[] args) throws Exception {
        System.out.println("Starting pid: " + ManagementFactory.getRuntimeMXBean().getName());

        long beginTime = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
        logger.info("application starts at {}", sdf.format(beginTime));

        startGCMonitoring();

//        int size = 50;   // -Xmx256m
//        int size = 1000;   // -Xmx8g
        int size = 2500;   // -Xmx16g

        int loopCounter = 100_000;
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("ru.otus:type=Benchmark");

        Benchmark mbean = new Benchmark(loopCounter);
        mbs.registerMBean(mbean, name);
        mbean.setSize(size);
        mbean.run();

    }

    private static void startGCMonitoring() {
        logger.info("App start time;App duration;GC start time;GC duration;" +
                "usedMemory Before;usedMemory After;Released usedMemory;commitedMemory After;" +
                "gcCause;gcName;gcAction");
        for (GarbageCollectorMXBean gcbean : ManagementFactory.getGarbageCollectorMXBeans()) {
            NotificationEmitter emitter = (NotificationEmitter) gcbean;
            emitter.addNotificationListener(listener, null, null);
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

    private static class BenchmarkMonitor implements NotificationListener {

        private volatile long appStartTime = 1;

        @Override
        public void handleNotification(Notification notification, Object handback) {
            if (notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
                GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());

                String gcName = info.getGcName();
                String gcAction = info.getGcAction();
                String gcCause = info.getGcCause();

                long gcStartTime = info.getGcInfo().getStartTime();
                long gcDuration = info.getGcInfo().getDuration();
                long usedMemoryBefore = 0;
                long usedMemoryAfter = 0;
                long commitedMemoryAfter = 0;

                List<String> listHeapMemTypes = new ArrayList<>();
                for(MemoryPoolMXBean mBean: ManagementFactory.getMemoryPoolMXBeans()) {
                    //Здесь составляем список mem с типами памяти MemoryType.HEAP
                    if (mBean.getType() == MemoryType.HEAP) {
                        listHeapMemTypes.add(mBean.getName());
                    }
                }

                // Далее для mapBefore и mapAfter посчитать сумму памяти HEAP типов
                // и вывести в лог в нужном формате

                Map<String, MemoryUsage> mapBefore = info.getGcInfo().getMemoryUsageBeforeGc();
                for (Map.Entry<String, MemoryUsage> entry : mapBefore.entrySet()) {
                    String memTypeName = entry.getKey();
                    if (!listHeapMemTypes.contains(memTypeName))
                        continue;
                    MemoryUsage memoryUsage = mapBefore.get(memTypeName);
                    usedMemoryBefore += memoryUsage.getUsed();
                }

                Map<String, MemoryUsage> mapAfter = info.getGcInfo().getMemoryUsageAfterGc();
                for (Map.Entry<String, MemoryUsage> entry : mapAfter.entrySet()) {
                    String memTypeName = entry.getKey();
                    if (!listHeapMemTypes.contains(memTypeName))
                        continue;
                    MemoryUsage memoryUsage = mapAfter.get(memTypeName);
                    usedMemoryAfter += memoryUsage.getUsed();
                    commitedMemoryAfter += memoryUsage.getCommitted();
                }

                long usedMemoryReleased = usedMemoryBefore - usedMemoryAfter;
                long appDuration = gcStartTime - appStartTime;
                logger.info("{};{};{};{};{};{};{};{};{};{};{}",
                        appStartTime,
                        appDuration,
                        gcStartTime,
                        gcDuration,
                        usedMemoryBefore  / 1024 / 1024,
                        usedMemoryAfter / 1024 / 1024,
                        usedMemoryReleased / 1024 / 1024,
                        commitedMemoryAfter / 1024 / 1024,
                        gcCause,
                        gcName,
                        gcAction);
                appStartTime = gcStartTime + gcDuration;

                logger.debug("start:" + gcStartTime +
                        " Name:" + gcName +
                        ", action:" + gcAction +
                        ", gcCause:" + gcCause +
                        "(" + gcDuration + " ms)");
            }
        }
    }
}
