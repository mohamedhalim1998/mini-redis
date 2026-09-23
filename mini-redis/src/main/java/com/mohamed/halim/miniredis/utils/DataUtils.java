package com.mohamed.halim.miniredis.utils;

public class DataUtils {
    private DataUtils() {

    }

    public static Long parse(String value) {
        try {
            return Long.parseLong(value);
        } catch (Exception e) {
            throw new RuntimeException("(error) value is not an integer or out of range");
        }
    }
}
