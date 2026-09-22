package com.mohamed.halim.miniredis.datastore;

/**
 * Represents a single entry in the data store, used for persistence serialization.
 *
 * @param key       the key name
 * @param type      the data type ("string", "list", "set", "hash")
 * @param value     the raw value (type-specific)
 * @param expiresAt absolute expiration timestamp in ms, or -1 if no expiry
 */
public record DataEntry(String key, String type, Object value, long expiresAt) {
}
