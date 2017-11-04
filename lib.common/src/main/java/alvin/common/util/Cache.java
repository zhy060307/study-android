package alvin.common.util;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Cache<T> {
    private Map<String, Entry<T>> cacheMap = new ConcurrentHashMap<>();

    private final Long expired;
    private final ChronoUnit unit;

    private final Integer maxSize;

    public Cache(Long expired, ChronoUnit unit) {
        this.expired = expired;
        this.unit = unit;
        this.maxSize = Integer.MAX_VALUE;
    }

    public Cache(Integer maxSize) {
        this.expired = Long.MAX_VALUE;
        this.unit = ChronoUnit.FOREVER;
        this.maxSize = maxSize;
    }

    public void put(String key, T cache) {
        cacheMap.put(key, new Entry<>(cache));
        trim();
    }

    private synchronized void trim() {
        Set<String> exceptKeys = new HashSet<>(trimBySize());
        exceptKeys.addAll(trimByTime());

        Map<String, Entry<T>> newCacheMap = new ConcurrentHashMap<>();
        cacheMap.forEach((key, entry) -> {
            if (!exceptKeys.contains(key)) {
                newCacheMap.put(key, entry);
            }
        });

        Map<String, Entry<T>> oldCacheMap = this.cacheMap;
        this.cacheMap = newCacheMap;
        oldCacheMap.clear();
    }

    private Collection<String> trimByTime() {
        final List<String> exceptKeys = new ArrayList<>();

        cacheMap.forEach((key, value) -> {
            if (value.isExpired(expired, unit)) {
                exceptKeys.add(key);
            }
        });

        return exceptKeys;
    }

    private Collection<String> trimBySize() {
        if (cacheMap.size() <= maxSize) {
            return Collections.emptyList();
        }

        final List<String> keys = new ArrayList<>(cacheMap.keySet());
        keys.sort((a, b) -> {
            Entry<T> ea = cacheMap.get(a);
            Entry<T> eb = cacheMap.get(b);
            if (ea == null) {
                return eb != null ? -1 : 0;
            } else if (eb == null) {
                return 1;
            } else {
                return ea.getTimestamp().compareTo(eb.getTimestamp());
            }
        });

        return keys.subList(0, maxSize - 1);
    }

    public Optional<T> get(String key) {
        Entry<T> entry = cacheMap.get(key);
        if (entry == null) {
            return Optional.empty();
        }
        return Optional.of(entry.getValue());
    }

    public void clear() {
        cacheMap.clear();
    }

    public boolean contains(String key) {
        return cacheMap.containsKey(key);
    }

    static class Entry<T> {
        private T value;
        private LocalDateTime timestamp;

        public Entry(T value) {
            this.value = value;
            this.timestamp = LocalDateTime.now();
        }

        public T getValue() {
            return value;
        }

        LocalDateTime getTimestamp() {
            return timestamp;
        }

        public boolean isExpired(long expired, ChronoUnit unit) {
            LocalDateTime expiredTime = timestamp.plus(expired, unit);
            return LocalDateTime.now().isAfter(expiredTime);
        }
    }
}
