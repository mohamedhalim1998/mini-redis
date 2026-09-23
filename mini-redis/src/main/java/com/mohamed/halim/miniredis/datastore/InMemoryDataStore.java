package com.mohamed.halim.miniredis.datastore;

import com.mohamed.halim.miniredis.utils.DataUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class InMemoryDataStore implements DataStore {
    private final Map<String, DataEntry> store = new ConcurrentHashMap<>();

    @Override
    public synchronized void set(String key, String value) {
        store.put(key, DataEntry.fromSimpleValue(key, value));
    }

    @Override
    public synchronized String get(String key) {
        var value = store.getOrDefault(key, DataEntry.empty());
        if(value.isExpired()) {
            del(key);
            return null;
        }
        return value.getSimpleValue();
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
        var value = store.get(key);
        if(value == null || value.ttl() == -1 || value.isExpired()) {
            return false;
        }
        store.put(key, value.cloneWithoutTtl());
        return true;
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
        var entries = store.values();
        var count = new AtomicInteger(0);
        entries.forEach(e -> {
            if(e.isExpired()) {
                store.remove(e.key());
                count.incrementAndGet();
            }
        });
        return count.intValue();
    }

    @Override
    public long rpush(String key, String... values) {
        var value = store.get(key);

        if(value != null && !DataEntry.DataType.LIST.equals(value.type())) {
            throw new WrongTypeException();
        }
        if(value == null) {
            value = DataEntry.initList(key, values);
            store.put(key, value);
        } else {
            value.appendItems(values);
        }
        return value.getListSize();
    }

    @Override
    public long lpush(String key, String... values) {
        var value = store.get(key);

        if(value != null && !DataEntry.DataType.LIST.equals(value.type())) {
            throw new WrongTypeException();
        }
        if(value == null) {
            value = DataEntry.initListReverse(key, values);
            store.put(key, value);
        } else {
            value.appendItemsFirst(values);
        }
        return value.getListSize();
    }

    @Override
    public String lpop(String key) {
        var value = store.get(key);
        if(value == null) {
            return null;
        }
        if(!DataEntry.DataType.LIST.equals(value.type())) {
            throw new WrongTypeException();
        }
        return value.removeFirst();
    }

    @Override
    public String rpop(String key) {
        var value = store.get(key);
        if(value == null) {
            return null;
        }
        if(!DataEntry.DataType.LIST.equals(value.type())) {
            throw new WrongTypeException();
        }
        return value.removeLast();
    }

    @Override
    public long llen(String key) {
        var value = store.get(key);
        if(value == null) {
            return 0;
        }
        if(!DataEntry.DataType.LIST.equals(value.type())) {
            throw new WrongTypeException();
        }
        return value.getListSize();
    }

    @Override
    public List<String> lrange(String key, long start, long stop) {
        var value = store.get(key);
        if(value == null) {
            return List.of();
        }
        if(!DataEntry.DataType.LIST.equals(value.type())) {
            throw new WrongTypeException();
        }
        var size = value.getListSize();
        start = start < 0 ? start + size : start;
        stop = stop < 0 ? stop + size : stop;
        return value.getRange(start, stop);
    }

    @Override
    public long sadd(String key, String... members) {
        var value = store.get(key);
        if(value != null && !DataEntry.DataType.SET.equals(value.type())) {
            throw new WrongTypeException();
        }
        if(value == null) {
            value = DataEntry.initSet(key, members);
            store.put(key, value);
            return value.getSetSize();
        } else {
            return value.addToSet(members);
        }
    }

    @Override
    public long srem(String key, String... members) {
        var value = store.get(key);
        if(value != null && !DataEntry.DataType.SET.equals(value.type())) {
            throw new WrongTypeException();
        }
        if(value == null) {
            return 0;
        } else {
            return value.removeFromSet(members);
        }
    }

    @Override
    public boolean sismember(String key, String member) {
        var value = store.get(key);
        if(value != null && !DataEntry.DataType.SET.equals(value.type())) {
            throw new WrongTypeException();
        }
        if(value == null) {
            return false;
        }
        return value.isSetMember(member);
    }

    @Override
    public Set<String> smembers(String key) {
        var value = store.get(key);
        if(value != null && !DataEntry.DataType.SET.equals(value.type())) {
            throw new WrongTypeException();
        }
        if(value == null) {
            return Set.of();
        }
        return value.getSetValue();
    }

    @Override
    public long scard(String key) {
        var value = store.get(key);
        if(value != null && !DataEntry.DataType.SET.equals(value.type())) {
            throw new WrongTypeException();
        }
        if(value == null) {
            return 0;
        }
        return value.getSetSize();
    }

    @Override
    public boolean hset(String key, String field, String value) {
        var keyValue = store.get(key);
        if(keyValue != null && !DataEntry.DataType.HASH.equals(keyValue.type())) {
            throw new WrongTypeException();
        }
        if(keyValue == null) {
            keyValue = DataEntry.initHash(key, field, value);
            store.put(key, keyValue);
            return true;
        } else {
            return keyValue.addToHash(field, value);
        }
    }

    @Override
    public String hget(String key, String field) {
        var keyValue = store.get(key);
        if(keyValue != null && !DataEntry.DataType.HASH.equals(keyValue.type())) {
            throw new WrongTypeException();
        }
        if(keyValue == null) {
            return null;
        } else {
            return keyValue.getFromHash(field);
        }
    }

    @Override
    public long hdel(String key, String... fields) {
        var keyValue = store.get(key);
        if(keyValue != null && !DataEntry.DataType.HASH.equals(keyValue.type())) {
            throw new WrongTypeException();
        }
        if(keyValue == null) {
            return 0;
        } else {
            return keyValue.removeFromHash(fields);
        }
    }

    @Override
    public Map<String, String> hgetall(String key) {
        var keyValue = store.get(key);
        if(keyValue != null && !DataEntry.DataType.HASH.equals(keyValue.type())) {
            throw new WrongTypeException();
        }
        if(keyValue == null) {
            return Map.of();
        } else {
            return keyValue.getMap();
        }
    }

    @Override
    public boolean hexists(String key, String field) {
        return hget(key, field) != null;
    }

    @Override
    public long hlen(String key) {
        return hgetall(key).size();
    }

    @Override
    public String type(String key) {
        var value = store.get(key);
        if(value == null) {
            return null;
        }
        return value.type().name();
    }

    @Override
    public Set<String> keys(String pattern) {
        var regex = Pattern.compile(pattern.replace(".", "\\.")
                .replace("*", ".*")
                .replace("?", "."));
        return store.keySet().stream().filter(key -> regex.matcher(key).matches()).collect(Collectors.toSet());
    }

    @Override
    public Iterable<DataEntry> entries() {
        return store.values();
    }

    @Override
    public long size() {
        return store.size();
    }
}
