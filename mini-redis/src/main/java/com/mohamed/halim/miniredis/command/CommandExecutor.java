package com.mohamed.halim.miniredis.command;

import java.util.List;

/**
 * Executes Redis commands against the data store.
 *
 * <p>Each command is received as a list of strings (command name + arguments)
 * and produces a RESP-encoded byte array response.
 */
public interface CommandExecutor {

    // --- Phase 1: Basic Commands ---

    /**
     * Execute a parsed command and produce the RESP response.
     *
     * <p>The command list contains the command name at index 0 (uppercase)
     * followed by arguments. For example: ["SET", "mykey", "myvalue"]
     *
     * <p>Returns the appropriate RESP-encoded response based on the command:
     * <ul>
     *   <li>PING → +PONG</li>
     *   <li>ECHO msg → $msg</li>
     *   <li>SET key value → +OK</li>
     *   <li>GET key → bulk string or null</li>
     *   <li>DEL key [key...] → integer (count of deleted)</li>
     *   <li>EXISTS key [key...] → integer (count of existing)</li>
     *   <li>INCR key → integer (new value)</li>
     *   <li>DECR key → integer (new value)</li>
     * </ul>
     *
     * @param command the command and arguments
     * @return RESP-encoded response bytes
     */
    byte[] execute(List<String> command);
}
