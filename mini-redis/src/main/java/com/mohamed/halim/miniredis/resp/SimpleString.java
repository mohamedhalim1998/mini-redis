package com.mohamed.halim.miniredis.resp;

/**
 * A simple string (first byte: '+').
 * Example: +OK\r\n
 */
public record SimpleString(String value) implements RespValue {
    @Override
    public RespType type() {
        return RespType.SIMPLE;
    }
}
