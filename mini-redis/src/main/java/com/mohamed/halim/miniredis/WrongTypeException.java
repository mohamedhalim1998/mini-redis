package com.mohamed.halim.miniredis;

/**
 * Thrown when a command is executed against a key holding the wrong data type.
 *
 * <p>Corresponds to Redis error: WRONGTYPE Operation against a key holding the wrong kind of value
 */
public class WrongTypeException extends RuntimeException {

    public WrongTypeException() {
        super("WRONGTYPE Operation against a key holding the wrong kind of value");
    }

    public WrongTypeException(String key, String expected, String actual) {
        super("WRONGTYPE Operation against a key holding the wrong kind of value. Key '%s' holds %s, expected %s"
                .formatted(key, actual, expected));
    }
}
