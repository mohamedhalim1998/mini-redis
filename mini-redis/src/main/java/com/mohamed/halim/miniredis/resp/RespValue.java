package com.mohamed.halim.miniredis.resp;

/**
 * Represents a parsed RESP protocol value.
 *
 * <p>This is a sealed hierarchy covering all RESP2 data types that
 * the parser can produce. Use pattern matching to handle each type.
 */
public sealed interface RespValue permits Array, BulkString, Error, Integer, SimpleString {
    RespType type();
}
