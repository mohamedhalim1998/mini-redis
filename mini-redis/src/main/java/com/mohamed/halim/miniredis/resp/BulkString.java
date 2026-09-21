package com.mohamed.halim.miniredis.resp;

/**
 * A bulk string (first byte: '$').
 * Can be null (represented as $-1\r\n).
 * Example: $5\r\nhello\r\n
 */
public record BulkString(String value) implements RespValue {
    public boolean isNull() {
        return value == null;
    }

    @Override
    public RespType type() {
        return RespType.BULK_STRING;
    }
}
