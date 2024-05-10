package entities;

import java.util.HashMap;

public class MemoryCache<K, V> {

    private final HashMap<K, V> cache;
    private final int capacity;

    public MemoryCache(int capacity) {
        this.capacity = capacity;
        this.cache = new HashMap<>();
    }

    public synchronized void put(K key, V value) {
        if (cache.size() >= capacity) {
            K lruKey = cache.keySet().iterator().next();
            cache.remove(lruKey);
        }
        cache.put(key, value);
    }

    public synchronized V get(K key) {
        return cache.get(key);
    }

    public synchronized void remove(K key) {
        cache.remove(key);
    }

    public synchronized void clear() {
        cache.clear();
    }

    public synchronized boolean containsKey(K key) {
        return cache.containsKey(key);
    }

    public synchronized boolean isEmpty() {
        return cache.isEmpty();
    }

    public synchronized int size() {
        return cache.size();
    }
}
