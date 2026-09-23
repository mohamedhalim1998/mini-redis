package com.mohamed.halim.miniredis.datastore;

/**
 * Represents a single entry in the data store, used for persistence serialization.
 *
 * @param key       the key name
 * @param type      the data type ("string", "list", "set", "hash")
 * @param value     the raw value (type-specific)
 * @param expiresAt absolute expiration timestamp in ms, or -1 if no expiry
 */
public record DataEntry(String key, DataType type, Object value, long ttl, long expiresAt) {
    public static DataEntry empty() {
        return new DataEntry("", DataType.NULL, null, -1, -1);
    }

    public String getSimpleValue() {
        if(type == DataType.SIMPLE && value != null) {
            return value.toString();
        }
        return null;
    }

    public long getRemainingTtl() {
        if(ttl == -1) {
            return -1;
        }
        var remainTtl = expiresAt - System.currentTimeMillis();
        if(remainTtl < 0) {
            return -2;
        }
        return remainTtl;
    }

    public boolean isExpired() {
        return ttl != -1 && System.currentTimeMillis() >= expiresAt;
    }

    public enum DataType {
        SIMPLE,
        LIST,
        HASH,
        NULL
    }

    public static DataEntry fromSimpleValue(String key, String value) {
        return new DataEntry(key,DataType.SIMPLE , value,-1,  -1);
    }

    public static DataEntry fromSimpleValue(String key, String value, long ttl) {
        return new DataEntry(key,DataType.SIMPLE , value,ttl, System.currentTimeMillis() + ttl);
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
}
