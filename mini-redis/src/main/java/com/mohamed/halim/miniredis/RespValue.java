package com.mohamed.halim.miniredis;

import java.util.List;

/**
 * Represents a parsed RESP protocol value.
 *
 * <p>This is a sealed hierarchy covering all RESP2 data types that
 * the parser can produce. Use pattern matching to handle each type.
 */
public sealed interface RespValue {

    /**
     * A simple string (first byte: '+').
     * Example: +OK\r\n
     */
    record SimpleString(String value) implements RespValue {}

    /**
     * An error (first byte: '-').
     * Example: -ERR unknown command\r\n
     */
    record Error(String type, String message) implements RespValue {}

    /**
     * An integer (first byte: ':').
     * Example: :1000\r\n
     */
    record Integer(long value) implements RespValue {}

    /**
     * A bulk string (first byte: '$').
     * Can be null (represented as $-1\r\n).
     * Example: $5\r\nhello\r\n
     */
    record BulkString(String value) implements RespValue {
        public boolean isNull() {
            return value == null;
        }
    }

    /**
     * An array (first byte: '*').
     * Can be null (represented as *-1\r\n).
     * Example: *2\r\n$3\r\nfoo\r\n$3\r\nbar\r\n
     */
    record Array(List<RespValue> elements) implements RespValue {
        public boolean isNull() {
            return elements == null;
        }
    }
}
