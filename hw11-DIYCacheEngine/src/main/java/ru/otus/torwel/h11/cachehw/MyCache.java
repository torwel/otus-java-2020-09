package ru.otus.torwel.h11.cachehw;

import java.util.ArrayList;
import java.util.Map;
import java.util.WeakHashMap;


/**
 * @author sergey
 * created on 14.12.18.
 */
public class MyCache<K, V> implements HwCache<K, V> {

    private final Map<K, V> cache  = new WeakHashMap<>();
    private final ArrayList<HwListener<K, V>> listeners = new ArrayList<>();

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
        listeners.forEach(listener -> listener.notify(key, value, "put value"));
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
}
