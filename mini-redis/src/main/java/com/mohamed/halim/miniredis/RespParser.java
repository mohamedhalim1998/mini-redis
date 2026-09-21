package com.mohamed.halim.miniredis;

import com.mohamed.halim.miniredis.resp.RespValue;

import java.io.InputStream;
import java.util.List;

/**
 * Parses the RESP (Redis Serialization Protocol) wire format.
 *
 * <p>RESP is a text-based protocol where each data type is identified by its
 * first byte: '+' (Simple String), '-' (Error), ':' (Integer),
 * '$' (Bulk String), '*' (Array).
 *
 * <p>Clients always send commands as RESP Arrays of Bulk Strings.
 * For example, {@code SET key value} is encoded as:
 * <pre>
 * *3\r\n$3\r\nSET\r\n$3\r\nkey\r\n$5\r\nvalue\r\n
 * </pre>
 */
public interface RespParser {

    // --- Phase 1: Core Parsing ---

    /**
     * Parse the next RESP value from the input stream.
     *
     * @param input the input stream to read from
     * @return the parsed RESP value
     * @throws RespParseException if the input is malformed
     */
    RespValue parse(InputStream input);

    /**
     * Parse a complete command (an array of bulk strings) from the input.
     *
     * @param input the input stream
     * @return list of strings representing the command and its arguments
     * @throws RespParseException if not a valid command format
     */
    List<String> parseCommand(InputStream input);
}
