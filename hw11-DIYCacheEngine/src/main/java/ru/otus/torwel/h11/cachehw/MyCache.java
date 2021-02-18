package ru.otus.torwel.h11.cachehw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Map;
import java.util.WeakHashMap;


/**
 * @author sergey
 * created on 14.12.18.
 */
public class MyCache<K, V> implements HwCache<K, V> {
    private final static Logger logger = LoggerFactory.getLogger(MyCache.class);

    private final Map<K, V> cache  = new WeakHashMap<>();
    private final ArrayList<HwListener<K, V>> listeners = new ArrayList<>();

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
        notifyListeners(key, value);
    }

    @Override
    public void remove(K key) {
        cache.remove(key);
    }

    @Override
    public V get(K key) {
        return cache.get(key);
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }

    @Override
    public String toString() {
        return "Cache size: " + cache.size();
    }

    private void notifyListeners(K key, V value) {
        for (HwListener<K, V> listener : listeners) {
            try {
                listener.notify(key, value, "put value");
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
}
