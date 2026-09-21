package com.mohamed.halim.miniredis.resp;

import java.util.List;

/**
 * An array (first byte: '*').
 * Can be null (represented as *-1\r\n).
 * Example: *2\r\n$3\r\nfoo\r\n$3\r\nbar\r\n
 */
public record Array(List<RespValue> elements) implements RespValue {
    public boolean isNull() {
        return elements == null;
    }

    @Override
    public RespType type() {
        return RespType.ARRAY;
    }
}
