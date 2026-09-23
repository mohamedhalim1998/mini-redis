package com.mohamed.halim.miniredis.datastore;

import com.mohamed.halim.miniredis.utils.DataUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryDataStore implements DataStore {
    private final Map<String, DataEntry> store = new ConcurrentHashMap<>();

    @Override
    public synchronized void set(String key, String value) {
        store.put(key, DataEntry.fromSimpleValue(key, value));
    }

    @Override
    public synchronized String get(String key) {
        return store.getOrDefault(key, DataEntry.empty()).getSimpleValue();
    }

    @Override
    public synchronized int del(String... keys) {
        return (int) Arrays.stream(keys).filter(k -> store.remove(k) != null).count();
    }

    @Override
    public synchronized int exists(String... keys) {
        return (int) Arrays.stream(keys).filter(store::containsKey).count();
    }

    @Override
    public synchronized long incr(String key) {
        var value = store.getOrDefault(key, DataEntry.empty()).getSimpleValue();
        if(value == null) {
           value = "0";
        }
        long number = DataUtils.parse(value);
        number++;
        set(key, String.valueOf(number));
        return number;
    }

    @Override
    public synchronized long decr(String key) {
        var value = store.getOrDefault(key, DataEntry.empty()).getSimpleValue();
        if(value == null) {
            value = "0";
        }
        long number = DataUtils.parse(value);
        number--;
        set(key, String.valueOf(number));
        return number;
    }

    @Override
    public synchronized void setWithTtl(String key, String value, long ttlMs) {
        store.put(key, DataEntry.fromSimpleValue(key, value, ttlMs));
    }

    @Override
    public synchronized boolean expire(String key, long ttlMs) {
        var value = store.get(key);
        if(value == null) {
            return false;
        }
        store.put(key, value.cloneWithNewTtl(ttlMs));
        return true;
    }

    @Override
    public boolean persist(String key) {
        return false;
    }

    @Override
    public long pttl(String key) {
        var value = store.get(key);
        if(value == null) {
            return -2;
        }
        return value.getRemainingTtl();
    }

    @Override
    public int expireActiveCycle() {
        return 0;
    }

    @Override
    public long lpush(String key, String... values) {
        return 0;
    }

    @Override
    public long rpush(String key, String... values) {
        return 0;
    }

    @Override
    public String lpop(String key) {
        return "";
    }

    @Override
    public String rpop(String key) {
        return "";
    }

    @Override
    public long llen(String key) {
        return 0;
    }

    @Override
    public List<String> lrange(String key, long start, long stop) {
        return List.of();
    }

    @Override
    public long sadd(String key, String... members) {
        return 0;
    }

    @Override
    public long srem(String key, String... members) {
        return 0;
    }

    @Override
    public boolean sismember(String key, String member) {
        return false;
    }

    @Override
    public Set<String> smembers(String key) {
        return Set.of();
    }

    @Override
    public long scard(String key) {
        return 0;
    }

    @Override
    public boolean hset(String key, String field, String value) {
        return false;
    }

    @Override
    public String hget(String key, String field) {
        return "";
    }

    @Override
    public long hdel(String key, String... fields) {
        return 0;
    }

    @Override
    public Map<String, String> hgetall(String key) {
        return Map.of();
    }

    @Override
    public boolean hexists(String key, String field) {
        return false;
    }

    @Override
    public long hlen(String key) {
        return 0;
    }

    @Override
    public String type(String key) {
        return "";
    }

    @Override
    public Set<String> keys(String pattern) {
        return Set.of();
    }

    @Override
    public Iterable<DataEntry> entries() {
        return null;
    }

    @Override
    public long size() {
        return 0;
    }
}
