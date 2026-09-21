package com.mohamed.halim.miniredis.resp;

import java.util.Objects;

public enum RespType {
    SIMPLE('+'),
    ERROR('-'),
    INTEGER(':'),
    BULK_STRING('$'),
    ARRAY('*');

    private final char prefix;

    RespType(char prefix) {
        this.prefix = prefix;
    }

    public char getPrefix() {
        return prefix;
    }

    public static RespType fromPrefix(char prefix) {
        for (var type : RespType.values()) {
            if(Objects.equals(prefix, type.prefix)) {
                return type;
            }
        }
        throw new IllegalArgumentException(String.valueOf(prefix));
    }
}
