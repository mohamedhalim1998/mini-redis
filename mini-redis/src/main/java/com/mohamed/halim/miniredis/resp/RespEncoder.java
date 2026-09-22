package com.mohamed.halim.miniredis.resp;

import java.util.List;

/**
 * Encodes values into RESP (Redis Serialization Protocol) wire format.
 *
 * <p>The encoder serializes Java values into RESP byte sequences that
 * Redis clients can understand.
 */
public interface RespEncoder {
    class Holder {
        private static final RespEncoder INSTANCE = new RespEncoderImpl();
    }

    static RespEncoder getInstance() {
        return Holder.INSTANCE;
    }
    // --- Phase 1: Core Encoding ---

    /**
     * Encode a simple string response (e.g., "OK").
     * Format: {@code +OK\r\n}
     *
     * @param value the string value (must not contain \r or \n)
     * @return RESP-encoded bytes
     */
    byte[] encodeSimpleString(String value);

    /**
     * Encode an error response.
     * Format: {@code -ERR message\r\n}
     *
     * @param errorType the error prefix (e.g., "ERR", "WRONGTYPE")
     * @param message   the error message
     * @return RESP-encoded bytes
     */
    byte[] encodeError(String errorType, String message);

    /**
     * Encode an integer response.
     * Format: {@code :1000\r\n}
     *
     * @param value the integer value
     * @return RESP-encoded bytes
     */
    byte[] encodeInteger(long value);

    /**
     * Encode a bulk string response.
     * Format: {@code $5\r\nhello\r\n}
     *
     * @param value the string value, or null for null bulk string ($-1\r\n)
     * @return RESP-encoded bytes
     */
    byte[] encodeBulkString(String value);

    /**
     * Encode an array response.
     * Format: {@code *2\r\n$3\r\nfoo\r\n$3\r\nbar\r\n}
     *
     * @param elements list of bulk strings, or null for null array (*-1\r\n)
     * @return RESP-encoded bytes
     */
    byte[] encodeArray(List<String> elements);

    // --- Phase 5: Pub/Sub Messages ---

    /**
     * Encode a pub/sub push message.
     * Format: array of ["message", channel, payload]
     *
     * @param channel the channel name
     * @param message the published message
     * @return RESP-encoded bytes
     */
    byte[] encodePubSubMessage(String channel, String message);
}
