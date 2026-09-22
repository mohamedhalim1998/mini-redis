package com.mohamed.halim.miniredis.datastore;

import com.mohamed.halim.miniredis.WrongTypeException;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The in-memory data store that holds all Redis keys and values.
 *
 * <p>This is the core state of the mini-redis server. All data operations
 * go through this interface. Implementations should be thread-safe if
 * the server handles multiple connections concurrently.
 */
public interface DataStore {
    class Holder {
        private static final DataStore INSTANCE = new InMemoryDataStore();
    }

    static DataStore getInstance() {
        return DataStore.Holder.INSTANCE;
    }
    // --- Phase 1: String Operations ---

    /**
     * Store a string value at the given key, overwriting any existing value.
     *
     * @param key   the key
     * @param value the string value
     */
    void set(String key, String value);

    /**
     * Retrieve the string value at the given key.
     *
     * @param key the key
     * @return the value, or null if key doesn't exist or is expired
     * @throws WrongTypeException if the key holds a non-string type
     */
    String get(String key);

    /**
     * Delete one or more keys.
     *
     * @param keys the keys to delete
     * @return the number of keys actually removed
     */
    int del(String... keys);

    /**
     * Check how many of the given keys exist.
     *
     * @param keys the keys to check
     * @return the count of keys that exist (and are not expired)
     */
    int exists(String... keys);

    /**
     * Increment the integer value of a key by 1.
     * If the key doesn't exist, sets it to 1.
     *
     * @param key the key
     * @return the new value after incrementing
     * @throws NumberFormatException if the value is not an integer
     * @throws WrongTypeException   if the key holds a non-string type
     */
    long incr(String key);

    /**
     * Decrement the integer value of a key by 1.
     * If the key doesn't exist, sets it to -1.
     *
     * @param key the key
     * @return the new value after decrementing
     * @throws NumberFormatException if the value is not an integer
     * @throws WrongTypeException   if the key holds a non-string type
     */
    long decr(String key);

    // --- Phase 2: Expiration ---

    /**
     * Store a string value with a time-to-live.
     *
     * @param key   the key
     * @param value the value
     * @param ttlMs time-to-live in milliseconds
     */
    void setWithTtl(String key, String value, long ttlMs);

    /**
     * Set the expiration on an existing key.
     *
     * @param key   the key
     * @param ttlMs time-to-live in milliseconds
     * @return true if the timeout was set, false if key doesn't exist
     */
    boolean expire(String key, long ttlMs);

    /**
     * Remove the expiration from a key.
     *
     * @param key the key
     * @return true if the timeout was removed, false if key doesn't exist or had no expiry
     */
    boolean persist(String key);

    /**
     * Get the remaining time-to-live of a key in milliseconds.
     *
     * @param key the key
     * @return TTL in ms, -1 if no expiry, -2 if key doesn't exist
     */
    long pttl(String key);

    /**
     * Run one cycle of active expiration: sample random keys and
     * remove those that have expired.
     *
     * @return the number of keys expired in this cycle
     */
    int expireActiveCycle();

    // --- Phase 3: List Operations ---

    /**
     * Push values to the head (left) of a list.
     *
     * @param key    the key
     * @param values one or more values to push
     * @return the length of the list after push
     * @throws WrongTypeException if key holds a non-list type
     */
    long lpush(String key, String... values);

    /**
     * Push values to the tail (right) of a list.
     *
     * @param key    the key
     * @param values one or more values to push
     * @return the length of the list after push
     * @throws WrongTypeException if key holds a non-list type
     */
    long rpush(String key, String... values);

    /**
     * Remove and return the first element of a list.
     *
     * @param key the key
     * @return the popped element, or null if list is empty or key doesn't exist
     * @throws WrongTypeException if key holds a non-list type
     */
    String lpop(String key);

    /**
     * Remove and return the last element of a list.
     *
     * @param key the key
     * @return the popped element, or null if list is empty or key doesn't exist
     * @throws WrongTypeException if key holds a non-list type
     */
    String rpop(String key);

    /**
     * Get the length of a list.
     *
     * @param key the key
     * @return the length, or 0 if key doesn't exist
     * @throws WrongTypeException if key holds a non-list type
     */
    long llen(String key);

    /**
     * Get a range of elements from a list.
     *
     * @param key   the key
     * @param start start index (0-based, negative counts from end)
     * @param stop  stop index (inclusive, negative counts from end)
     * @return list of elements in the range, or empty list
     * @throws WrongTypeException if key holds a non-list type
     */
    List<String> lrange(String key, long start, long stop);

    // --- Phase 3: Set Operations ---

    /**
     * Add members to a set.
     *
     * @param key     the key
     * @param members one or more members to add
     * @return the number of members actually added (not already present)
     * @throws WrongTypeException if key holds a non-set type
     */
    long sadd(String key, String... members);

    /**
     * Remove members from a set.
     *
     * @param key     the key
     * @param members one or more members to remove
     * @return the number of members actually removed
     * @throws WrongTypeException if key holds a non-set type
     */
    long srem(String key, String... members);

    /**
     * Check if a value is a member of a set.
     *
     * @param key    the key
     * @param member the value to check
     * @return true if the member exists in the set
     * @throws WrongTypeException if key holds a non-set type
     */
    boolean sismember(String key, String member);

    /**
     * Get all members of a set.
     *
     * @param key the key
     * @return all members, or empty set if key doesn't exist
     * @throws WrongTypeException if key holds a non-set type
     */
    Set<String> smembers(String key);

    /**
     * Get the number of members in a set.
     *
     * @param key the key
     * @return the cardinality, or 0 if key doesn't exist
     * @throws WrongTypeException if key holds a non-set type
     */
    long scard(String key);

    // --- Phase 3: Hash Operations ---

    /**
     * Set a field in a hash.
     *
     * @param key   the key
     * @param field the field name
     * @param value the field value
     * @return true if this is a new field, false if it was updated
     * @throws WrongTypeException if key holds a non-hash type
     */
    boolean hset(String key, String field, String value);

    /**
     * Get a field value from a hash.
     *
     * @param key   the key
     * @param field the field name
     * @return the value, or null if field or key doesn't exist
     * @throws WrongTypeException if key holds a non-hash type
     */
    String hget(String key, String field);

    /**
     * Delete fields from a hash.
     *
     * @param key    the key
     * @param fields one or more field names
     * @return the number of fields actually removed
     * @throws WrongTypeException if key holds a non-hash type
     */
    long hdel(String key, String... fields);

    /**
     * Get all fields and values in a hash.
     *
     * @param key the key
     * @return map of field-value pairs, or empty map
     * @throws WrongTypeException if key holds a non-hash type
     */
    Map<String, String> hgetall(String key);

    /**
     * Check if a field exists in a hash.
     *
     * @param key   the key
     * @param field the field name
     * @return true if the field exists
     * @throws WrongTypeException if key holds a non-hash type
     */
    boolean hexists(String key, String field);

    /**
     * Get the number of fields in a hash.
     *
     * @param key the key
     * @return the number of fields, or 0 if key doesn't exist
     * @throws WrongTypeException if key holds a non-hash type
     */
    long hlen(String key);

    // --- Phase 3: Key Utilities ---

    /**
     * Get the type of value stored at key.
     *
     * @param key the key
     * @return "string", "list", "set", "hash", or "none" if key doesn't exist
     */
    String type(String key);

    /**
     * Find all keys matching the given glob-style pattern.
     *
     * @param pattern glob pattern (supports *, ?, [abc])
     * @return set of matching keys
     */
    Set<String> keys(String pattern);

    // --- Phase 4: Persistence Helpers ---

    /**
     * Get all key-value entries for serialization (persistence).
     * This should include type information and TTL data.
     *
     * @return an iterable of all entries in the store
     */
    Iterable<DataEntry> entries();

    /**
     * Get the total number of keys in the store (non-expired).
     *
     * @return the key count
     */
    long size();
}
