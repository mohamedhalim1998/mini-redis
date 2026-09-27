package com.mohamed.halim.miniredis.datastore;

import com.mohamed.halim.miniredis.utils.DataUtils;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents a single entry in the data store, used for persistence serialization.
 *
 * @param key       the key name
 * @param type      the data type ("string", "list", "set", "hash")
 * @param value     the raw value (type-specific)
 * @param expiresAt absolute expiration timestamp in ms, or -1 if no expiry
 */
public record DataEntry(String key, DataType type, Serializable value, long ttl, long expiresAt) implements Serializable {

    public String getSimpleValue() {
        if (type == DataType.SIMPLE && value != null) {
            return value.toString();
        }
        return null;
    }

    public long getRemainingTtl() {
        if (ttl == -1) {
            return -1;
        }
        var remainTtl = expiresAt - System.currentTimeMillis();
        if (remainTtl < 0) {
            return -2;
        }
        return remainTtl;
    }

    public boolean isExpired() {
        return ttl != -1 && System.currentTimeMillis() >= expiresAt;
    }

    public long getListSize() {
        if (DataType.LIST.equals(type)) {
            return ((LinkedList<?>) value).size();
        }
        return 0;
    }

    public void appendItems(String[] values) {
        if (value instanceof LinkedList<?>) {
            @SuppressWarnings("unchecked")
            LinkedList<String> list = (LinkedList<String>) value;
            Collections.addAll(list, values);
        }
    }

    public void appendItemsFirst(String[] values) {
        if (value instanceof LinkedList<?>) {
            @SuppressWarnings("unchecked")
            LinkedList<String> list = (LinkedList<String>) value;
            for (String s : values) {
                list.addFirst(s);
            }
        }
    }

    public String removeFirst() {
        if (type != DataType.LIST) {
            return null;
        }
        @SuppressWarnings("unchecked")
        LinkedList<String> list = (LinkedList<String>) value;
        return list.removeFirst();
    }

    public String removeLast() {
        if (type != DataType.LIST) {
            return null;
        }
        @SuppressWarnings("unchecked")
        LinkedList<String> list = (LinkedList<String>) value;
        return list.removeLast();
    }

    public List<String> getRange(long start, long stop) {
        if (type != DataType.LIST) {
            return List.of();
        }
        @SuppressWarnings("unchecked")
        LinkedList<String> list = (LinkedList<String>) value;
        return list.subList((int) start, (int) (stop + 1));
    }

    public long addToSet(String[] members) {
        if (type != DataType.SET) {
            return 0;
        }
        @SuppressWarnings("unchecked")
        Set<String> set = (HashSet<String>) value;

        return Arrays.stream(members).filter(set::add).count();

    }

    public long getSetSize() {
        if (DataType.SET.equals(type)) {
            return ((HashSet<?>) value).size();
        }
        return 0;
    }

    public long removeFromSet(String[] members) {
        if (type != DataType.SET) {
            return 0;
        }
        @SuppressWarnings("unchecked")
        Set<String> set = (HashSet<String>) value;
        return Arrays.stream(members).filter(set::remove).count();

    }

    public boolean isSetMember(String member) {
        if (type != DataType.SET) {
            return false;
        }
        @SuppressWarnings("unchecked")
        Set<String> set = (HashSet<String>) value;
        return set.contains(member);
    }

    public Set<String> getSetValue() {
        if (type != DataType.SET) {
            return Set.of();
        }
        @SuppressWarnings("unchecked")
        Set<String> set = (HashSet<String>) value;
        return set;
    }

    public boolean addToHash(String field, String value) {
        if (type != DataType.HASH) {
            return false;
        }
        @SuppressWarnings("unchecked")
        var map = (HashMap<String, String>) this.value;
        return map.put(field, value) == null;
    }

    public String getFromHash(String field) {
        if (type != DataType.HASH) {
            return null;
        }
        @SuppressWarnings("unchecked")
        var map = (HashMap<String, String>) this.value;
        return map.get(field);
    }

    public long removeFromHash(String[] fields) {
        if (type != DataType.HASH) {
            return 0;
        }
        @SuppressWarnings("unchecked")
        var map = (HashMap<String, String>) this.value;
        return Arrays.stream(fields).filter(e -> map.remove(e) != null).count();
    }


    public DataEntry cloneWithNewTtl(long ttl) {
        return new DataEntry(
                key,
                type,
                value,
                ttl,
                System.currentTimeMillis() + ttl
        );
    }

    public DataEntry cloneWithoutTtl() {
        return new DataEntry(
                key,
                type,
                value,
                -1,
                -1
        );
    }

    public Map<String, String> getMap() {
        if (type != DataType.HASH) {
            return null;
        }
        @SuppressWarnings("unchecked")
        var map = (HashMap<String, String>) this.value;
        return map;
    }

    public List<String> convertToCommand() {
        return switch (type) {
            case SIMPLE -> convertSimpleToCommand();
            case LIST -> convertListToCommand();
            case HASH -> convertHashToCommand();
            case SET -> convertSetToCommand();
            case NULL -> List.of();
        };
    }

    private List<String> convertSetToCommand() {
        var commands = new ArrayList<String>();
        commands.add("Sadd %s %s".formatted(key, String.join(" ", getSetValue())));
        if (isExpirable()) {
            commands.add("PEXPIREAT %s %d".formatted(key, expiresAt));
        }
        return commands;

    }

    private List<String> convertHashToCommand() {
        var commands = new ArrayList<String>();
        assert getMap() != null;
        getMap().forEach((k, v) -> {
            commands.add(
                    "HSET %s %s %s".formatted(key, k, v)
            );
        });
        if (isExpirable()) {
            commands.add("PEXPIREAT %s %d".formatted(key, expiresAt));
        }
        return commands;
    }

    private List<String> convertSimpleToCommand() {
        var commands = new ArrayList<String>();
        commands.add("SET %s %s".formatted(key, getSimpleValue()));
        if (isExpirable()) {
            commands.add("PEXPIREAT %s %d".formatted(key, expiresAt));
        }
        return commands;
    }

    private List<String> convertListToCommand() {
        var commands = new ArrayList<String>();
        commands.add("LPUSH %s %s".formatted(key, String.join(" ", getListValue())));
        if (isExpirable()) {
            commands.add("PEXPIREAT %s %d".formatted(key, expiresAt));
        }
        return commands;
    }

    private boolean isExpirable() {
        return ttl != -1;
    }

    private List<String> getListValue() {
        if (type != DataType.LIST) {
            return List.of();
        }
        @SuppressWarnings("unchecked")
        LinkedList<String> list = (LinkedList<String>) value;
        return list;
    }

    public enum DataType {
        SIMPLE,
        LIST,
        HASH,
        SET, NULL
    }

    public static DataEntry empty() {
        return new DataEntry("", DataType.NULL, null, -1, -1);
    }

    public static DataEntry fromSimpleValue(String key, String value) {
        return new DataEntry(key, DataType.SIMPLE, value, -1, -1);
    }

    public static DataEntry fromSimpleValue(String key, String value, long ttl) {
        return new DataEntry(key, DataType.SIMPLE, value, ttl, System.currentTimeMillis() + ttl);
    }

    public static DataEntry initSet(String key, String[] values) {
        var value = Arrays.stream(values).collect(Collectors.toCollection(HashSet::new));
        return new DataEntry(
                key,
                DataType.SET,
                value,
                -1,
                -1
        );
    }

    public static DataEntry initList(String key, String[] values) {
        var value = Arrays.stream(values).collect(Collectors.toCollection(LinkedList::new));
        return new DataEntry(
                key,
                DataType.LIST,
                value,
                -1,
                -1
        );
    }

    public static DataEntry initListReverse(String key, String[] values) {
        var value = new LinkedList<>(Arrays.stream(values).toList().reversed());
        return new DataEntry(
                key,
                DataType.LIST,
                value,
                -1,
                -1
        );
    }

    public static DataEntry initHash(String key, String field, String value) {
        var map = new HashMap<String, String>();
        map.put(field, value);
        return new DataEntry(
                key,
                DataType.HASH,
                map,
                -1,
                -1
        );
    }

}
