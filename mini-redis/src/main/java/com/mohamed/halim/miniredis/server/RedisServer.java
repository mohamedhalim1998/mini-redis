package com.mohamed.halim.miniredis.server;

/**
 * The main Redis server that listens for TCP connections and processes commands.
 *
 * <p>Implements a single-threaded event loop that accepts connections,
 * reads RESP-encoded commands, executes them, and writes responses.
 */
public interface RedisServer {

    // --- Phase 1: Server Lifecycle ---

    /**
     * Start the server, binding to the specified port.
     * This method should be non-blocking — it starts the event loop
     * in the background and returns immediately.
     **/
    void start();

    /**
     * Stop the server gracefully, closing all client connections
     * and releasing resources.
     */
    void stop();
}
