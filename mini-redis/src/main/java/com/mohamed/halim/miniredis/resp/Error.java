package com.mohamed.halim.miniredis.resp;

/**
 * An error (first byte: '-').
 * Example: -ERR unknown command\r\n
 */
public record Error(String message) implements RespValue {
    @Override
    public RespType type() {
        return RespType.SIMPLE;
    }
}
