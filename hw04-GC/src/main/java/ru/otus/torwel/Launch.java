package ru.otus.torwel;

import com.sun.management.GarbageCollectionNotificationInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.*;
import javax.management.openmbean.CompositeData;
import java.lang.management.*;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
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

    private static void startGCMonitoring() {
        logger.info("GC start time;GC duration;gcCause;gcName;gcAction");
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

/*
                Map<String, MemoryUsage> memBefore = info.getGcInfo().getMemoryUsageBeforeGc();
                Map<String, MemoryUsage> memAfter = info.getGcInfo().getMemoryUsageAfterGc();
                for (Map.Entry<String, MemoryUsage> entry : memAfter.entrySet()) {
                    String memType = entry.getKey();
                    MemoryUsage memdetail = entry.getValue();
                    long memInit = memdetail.getInit();
                    long memCommitted = memdetail.getCommitted();
                    long memMax = memdetail.getMax();
                    long memUsed = memdetail.getUsed();
                    MemoryUsage before = memBefore.get(memType);
                    long beforepercent = ((before.getUsed()*1000L)/before.getCommitted());
                    long percent = ((memUsed*1000L)/before.getCommitted()); //>100% when it gets expanded
                    System.out.print(memType + (memCommitted==memMax?"(fully expanded)":"(still expandable)") +"used: "+(beforepercent/10)+"."+(beforepercent%10)+"%->"+(percent/10)+"."+(percent%10)+"%("+((memUsed/1048576)+1)+"MB)\n");
                }
*/

                for(MemoryPoolMXBean mBean: ManagementFactory.getMemoryPoolMXBeans()) {
                    System.out.println(mBean.getName() + " - " + mBean.getType());
                    //TODO: Здесь составляем список mem с типами памяти MemoryType.HEAP
                    }
                System.out.println();

                // TODO: Далее для mapBefore и mapAfter посчитать сумму память HEAP типов
                // и вывести в лог в нужном формате

                Map mapBefore = info.getGcInfo().getMemoryUsageBeforeGc();
                Map mapAfter = info.getGcInfo().getMemoryUsageAfterGc();
                System.out.println("Before GC Memory Usage Details....");
                Set memType = mapBefore.keySet();
                Iterator it = memType.iterator();
                while(it.hasNext()) {
                    String type = (String)it.next();
                    System.out.println(type);
                    MemoryUsage mu1 = (MemoryUsage) mapBefore.get(type);
                    System.out.print("Initial Size: "+mu1.getInit());
                    System.out.print(" Used: "+ mu1.getUsed());
                    System.out.print(" Max: "+mu1.getMax());
                    System.out.print(" Committed: "+mu1.getCommitted());
                    System.out.println("");
                }
                System.out.println();
                System.out.println("After GC Memory Usage Details....");
                memType = mapAfter.keySet();
                it = memType.iterator();
                while(it.hasNext()) {
                    String type = (String)it.next();
                    System.out.println(type);
                    MemoryUsage mu2 = (MemoryUsage) mapAfter.get(type);
                    System.out.print("Initial Size: "+mu2.getInit());
                    System.out.print(" Used: "+ mu2.getUsed());
                    System.out.print(" Max: "+mu2.getMax());
                    System.out.print(" Committed: "+mu2.getCommitted());
                    System.out.println("");
                }
                System.out.println();

                logger.info("{};{};{};{};{}", startTime, duration, gcCause, gcName, gcAction);
                logger.debug("start:" + startTime + " Name:" + gcName + ", action:" + gcAction + ", gcCause:" + gcCause + "(" + duration + " ms)");
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
