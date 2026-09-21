package phase4;

import com.mohamed.halim.miniredis.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Tag("phase4")
@DisplayName("Persistence - AOF & RDB")
class PersistenceTest {

    // TODO: Replace with your implementations
    // private DataStore store;
    // private CommandExecutor executor;
    // private Persistence persistence;

    @TempDir
    Path tempDir;

    // @BeforeEach
    // void setUp() {
    //     store = new MiniRedisDataStore();
    //     executor = new MiniRedisCommandExecutor(store);
    //     persistence = new MiniRedisPersistence(tempDir.resolve("appendonly.aof"));
    // }

    @Test
    @DisplayName("should append write command to AOF file")
    void shouldAppendToAof() throws IOException {
        // persistence.appendToAof(List.of("SET", "key", "value"));
        // persistence.fsync();
        // Path aofFile = tempDir.resolve("appendonly.aof");
        // assertThat(Files.exists(aofFile)).isTrue();
        // String content = Files.readString(aofFile);
        // assertThat(content).contains("SET").contains("key").contains("value");
        fail("Implement Persistence and uncomment this test");
    }

    @Test
    @DisplayName("should replay AOF and restore state")
    void shouldReplayAof() throws IOException {
        // persistence.appendToAof(List.of("SET", "a", "1"));
        // persistence.appendToAof(List.of("SET", "b", "2"));
        // persistence.appendToAof(List.of("DEL", "a"));
        // persistence.fsync();
        //
        // // Create fresh store and replay
        // DataStore freshStore = new MiniRedisDataStore();
        // CommandExecutor freshExecutor = new MiniRedisCommandExecutor(freshStore);
        // Persistence freshPersistence = new MiniRedisPersistence(tempDir.resolve("appendonly.aof"));
        // int count = freshPersistence.replayAof(freshExecutor);
        //
        // assertThat(count).isEqualTo(3);
        // assertThat(freshStore.get("a")).isNull();
        // assertThat(freshStore.get("b")).isEqualTo("2");
        fail("Implement Persistence and uncomment this test");
    }

    @Test
    @DisplayName("should save and load RDB snapshot")
    void shouldSaveAndLoadRdb() throws IOException {
        // store.set("key1", "value1");
        // store.set("key2", "value2");
        //
        // Path rdbFile = tempDir.resolve("dump.rdb");
        // persistence.saveRdb(store, rdbFile);
        // assertThat(Files.exists(rdbFile)).isTrue();
        //
        // DataStore freshStore = new MiniRedisDataStore();
        // persistence.loadRdb(freshStore, rdbFile);
        //
        // assertThat(freshStore.get("key1")).isEqualTo("value1");
        // assertThat(freshStore.get("key2")).isEqualTo("value2");
        fail("Implement Persistence and uncomment this test");
    }

    @Test
    @DisplayName("should preserve TTL in RDB snapshot")
    void shouldPreserveTtlInRdb() throws IOException {
        // store.setWithTtl("expiring", "value", 60000L);
        //
        // Path rdbFile = tempDir.resolve("dump.rdb");
        // persistence.saveRdb(store, rdbFile);
        //
        // DataStore freshStore = new MiniRedisDataStore();
        // persistence.loadRdb(freshStore, rdbFile);
        //
        // assertThat(freshStore.get("expiring")).isEqualTo("value");
        // assertThat(freshStore.pttl("expiring")).isGreaterThan(0);
        fail("Implement Persistence and uncomment this test");
    }

    @Test
    @DisplayName("should rewrite AOF with minimal commands")
    void shouldRewriteAof() throws IOException {
        // // Write multiple redundant commands
        // persistence.appendToAof(List.of("SET", "key", "1"));
        // persistence.appendToAof(List.of("SET", "key", "2"));
        // persistence.appendToAof(List.of("SET", "key", "3"));
        // persistence.fsync();
        //
        // store.set("key", "3");
        // persistence.rewriteAof(store);
        //
        // // Rewritten AOF should be smaller and contain only final state
        // DataStore freshStore = new MiniRedisDataStore();
        // CommandExecutor freshExecutor = new MiniRedisCommandExecutor(freshStore);
        // Persistence freshPersistence = new MiniRedisPersistence(tempDir.resolve("appendonly.aof"));
        // int count = freshPersistence.replayAof(freshExecutor);
        //
        // assertThat(count).isEqualTo(1); // only one SET command for the key
        // assertThat(freshStore.get("key")).isEqualTo("3");
        fail("Implement Persistence and uncomment this test");
    }

    @Test
    @DisplayName("should return 0 when replaying non-existent AOF")
    void shouldReturnZeroForMissingAof() throws IOException {
        // Persistence freshPersistence = new MiniRedisPersistence(tempDir.resolve("nonexistent.aof"));
        // int count = freshPersistence.replayAof(executor);
        // assertThat(count).isEqualTo(0);
        fail("Implement Persistence and uncomment this test");
    }
}
