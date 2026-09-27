package phase4;

import com.mohamed.halim.miniredis.command.CommandExecutor;
import com.mohamed.halim.miniredis.command.CommandExecutorImpl;
import com.mohamed.halim.miniredis.datastore.DataStore;
import com.mohamed.halim.miniredis.datastore.InMemoryDataStore;
import com.mohamed.halim.miniredis.persistence.Persistence;
import com.mohamed.halim.miniredis.persistence.PersistenceImpl;
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

    private DataStore store;
    private CommandExecutor executor;
    private Persistence persistence;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        store = DataStore.getInstance();
        executor = new CommandExecutorImpl();
        persistence = new PersistenceImpl(tempDir.resolve("appendonly.aof"));
    }

    @Test
    @DisplayName("should append write command to AOF file")
    void shouldAppendToAof() throws IOException {
        persistence.appendToAof(List.of("SET", "key", "value"));
        persistence.fsync();
        Path aofFile = tempDir.resolve("appendonly.aof");
        assertThat(Files.exists(aofFile)).isTrue();
        String content = Files.readString(aofFile);
        assertThat(content).contains("SET").contains("key").contains("value");

    }

    @Test
    @DisplayName("should replay AOF and restore state")
    void shouldReplayAof() throws IOException {
        persistence.appendToAof(List.of("SET", "a", "1"));
        persistence.appendToAof(List.of("SET", "b", "2"));
        persistence.appendToAof(List.of("DEL", "a"));
        persistence.fsync();
        store.clear();
        Path aofFile = tempDir.resolve("appendonly.aof");
        Persistence freshPersistence = new PersistenceImpl(aofFile);
        int count = freshPersistence.replayAof(executor);

        assertThat(count).isEqualTo(3);
        assertThat(store.get("a")).isNull();
        assertThat(store.get("b")).isEqualTo("2");

    }

    @Test
    @DisplayName("should save and load RDB snapshot")
    void shouldSaveAndLoadRdb() throws IOException {
        store.set("key1", "value1");
        store.set("key2", "value2");

        Path rdbFile = tempDir.resolve("dump.rdb");
        persistence.saveRdb(store, rdbFile);
        assertThat(Files.exists(rdbFile)).isTrue();

        DataStore freshStore = new InMemoryDataStore();
        persistence.loadRdb(freshStore, rdbFile);

        assertThat(freshStore.get("key1")).isEqualTo("value1");
        assertThat(freshStore.get("key2")).isEqualTo("value2");

    }

    @Test
    @DisplayName("should preserve TTL in RDB snapshot")
    void shouldPreserveTtlInRdb() throws IOException {
        store.setWithTtl("expiring", "value", 60000L);

        Path rdbFile = tempDir.resolve("dump.rdb");
        persistence.saveRdb(store, rdbFile);

        DataStore freshStore = new InMemoryDataStore();
        persistence.loadRdb(freshStore, rdbFile);

        assertThat(freshStore.get("expiring")).isEqualTo("value");
        assertThat(freshStore.pttl("expiring")).isGreaterThan(0);

    }

    @Test
    @DisplayName("should rewrite AOF with minimal commands")
    void shouldRewriteAof() throws IOException {
        store.clear();
        persistence.appendToAof(List.of("SET", "key", "1"));
        persistence.appendToAof(List.of("SET", "key", "2"));
        persistence.appendToAof(List.of("SET", "key", "3"));
        persistence.fsync();

        store.set("key", "3");
        persistence.rewriteAof(store);
        store.clear();
        Path aofFile = tempDir.resolve("appendonly.aof");
        Persistence freshPersistence = new PersistenceImpl(aofFile);
        int count = freshPersistence.replayAof(executor);

        assertThat(count).isEqualTo(1);
        assertThat(store.get("key")).isEqualTo("3");

    }

    @Test
    @DisplayName("should return 0 when replaying non-existent AOF")
    void shouldReturnZeroForMissingAof() throws IOException {
        Persistence freshPersistence = new PersistenceImpl();
        int count = freshPersistence.replayAof(executor);
        assertThat(count).isEqualTo(0);

    }
}
