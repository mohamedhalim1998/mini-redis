package com.mohamed.halim.miniredis.resp;

/**
 * An integer (first byte: ':').
 * Example: :1000\r\n
 */
public record Integer(long value) implements RespValue {
    @Override
    public RespType type() {
        return RespType.INTEGER;
    }
}
