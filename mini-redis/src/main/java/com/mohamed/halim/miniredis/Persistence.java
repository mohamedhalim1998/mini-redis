package com.mohamed.halim.miniredis;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Handles data persistence to disk via AOF and RDB mechanisms.
 */
public interface Persistence {

    // --- Phase 4: AOF ---

    /**
     * Append a write command to the AOF (Append-Only File).
     * The command is stored in RESP format.
     *
     * @param command the command and arguments (e.g., ["SET", "key", "value"])
     * @throws IOException if writing to disk fails
     */
    void appendToAof(List<String> command) throws IOException;

    /**
     * Replay the AOF file, executing all stored commands against the data store
     * to reconstruct state on startup.
     *
     * @param executor the command executor to replay commands through
     * @return the number of commands replayed
     * @throws IOException if reading the file fails
     */
    int replayAof(CommandExecutor executor) throws IOException;

    /**
     * Rewrite the AOF file by compacting it: generate minimal commands
     * that represent the current state of the data store.
     *
     * @param store the current data store to serialize
     * @throws IOException if writing fails
     */
    void rewriteAof(DataStore store) throws IOException;

    /**
     * Flush the AOF buffer to disk according to the configured fsync policy.
     *
     * @throws IOException if fsync fails
     */
    void fsync() throws IOException;

    // --- Phase 4: RDB ---

    /**
     * Save the current data store state to an RDB snapshot file.
     *
     * @param store the data store to snapshot
     * @param path  the file path to write to
     * @throws IOException if serialization or writing fails
     */
    void saveRdb(DataStore store, Path path) throws IOException;

    /**
     * Load an RDB snapshot file and restore state into the data store.
     *
     * @param store the data store to load into
     * @param path  the RDB file to read
     * @throws IOException if reading or deserialization fails
     */
    void loadRdb(DataStore store, Path path) throws IOException;
}
