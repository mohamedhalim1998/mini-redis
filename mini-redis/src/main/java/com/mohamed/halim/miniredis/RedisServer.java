package com.mohamed.halim.miniredis;

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
     *
     * @param port the TCP port to listen on (default: 6379)
     */
    void start(int port);

    /**
     * Stop the server gracefully, closing all client connections
     * and releasing resources.
     */
    void stop();

    /**
     * Check if the server is currently running and accepting connections.
     *
     * @return true if the server is running
     */
    boolean isRunning();

    /**
     * Get the port the server is listening on.
     *
     * @return the bound port, or -1 if not started
     */
    int getPort();
}
