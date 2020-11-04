package ru.otus.torwel;

import com.sun.management.GarbageCollectionNotificationInfo;

import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;

public class BenchmarkMonitor implements NotificationListener {

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

            System.out.println("start:" + startTime + " Name:" + gcName + ", action:" + gcAction + ", gcCause:" + gcCause + "(" + duration + " ms)");
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
