package com.mohamed.halim.miniredis.resp;

/**
 * Thrown when the RESP parser encounters malformed input.
 */
public class RespParseException extends RuntimeException {

    public RespParseException(String message) {
        super(message);
    }

    public RespParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
