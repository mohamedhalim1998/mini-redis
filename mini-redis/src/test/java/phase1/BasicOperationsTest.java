package phase1;

import com.mohamed.halim.miniredis.*;
import com.mohamed.halim.miniredis.datastore.DataStore;
import com.mohamed.halim.miniredis.datastore.InMemoryDataStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@Tag("phase1")
@DisplayName("Basic String Operations")
class BasicOperationsTest {

     private DataStore store;

     @BeforeEach
     void setUp() {
         store = new InMemoryDataStore();
     }

    @Test
    @DisplayName("should store and retrieve a value")
    void shouldStoreAndRetrieveValue() {
         store.set("key", "value");
         assertThat(store.get("key")).isEqualTo("value");
        
    }

    @Test
    @DisplayName("should return null for missing key")
    void shouldReturnNullForMissingKey() {
         assertThat(store.get("nonexistent")).isNull();
        
    }

    @Test
    @DisplayName("should overwrite existing value")
    void shouldOverwriteExistingValue() {
         store.set("key", "first");
         store.set("key", "second");
         assertThat(store.get("key")).isEqualTo("second");
        
    }

    @Test
    @DisplayName("should delete existing key")
    void shouldDeleteExistingKey() {
         store.set("key", "value");
         int deleted = store.del("key");
         assertThat(deleted).isEqualTo(1);
         assertThat(store.get("key")).isNull();
        
    }

    @Test
    @DisplayName("should return 0 when deleting non-existent key")
    void shouldReturnZeroWhenDeletingNonExistent() {
         int deleted = store.del("nonexistent");
         assertThat(deleted).isEqualTo(0);
        
    }

    @Test
    @DisplayName("should delete multiple keys")
    void shouldDeleteMultipleKeys() {
         store.set("a", "1");
         store.set("b", "2");
         store.set("c", "3");
         int deleted = store.del("a", "b", "nonexistent");
         assertThat(deleted).isEqualTo(2);
        
    }

    @Test
    @DisplayName("should check if key exists")
    void shouldCheckKeyExists() {
         store.set("key", "value");
         assertThat(store.exists("key")).isEqualTo(1);
         assertThat(store.exists("nonexistent")).isEqualTo(0);
        
    }

    @Test
    @DisplayName("should count multiple existing keys")
    void shouldCountMultipleExistingKeys() {
         store.set("a", "1");
         store.set("b", "2");
         assertThat(store.exists("a", "b", "c")).isEqualTo(2);
        
    }

    @Test
    @DisplayName("should increment value")
    void shouldIncrementValue() {
         store.set("counter", "10");
         long result = store.incr("counter");
         assertThat(result).isEqualTo(11);
         assertThat(store.get("counter")).isEqualTo("11");
        
    }

    @Test
    @DisplayName("should increment non-existent key from zero")
    void shouldIncrementFromZero() {
         long result = store.incr("counter");
         assertThat(result).isEqualTo(1);
        
    }

    @Test
    @DisplayName("should decrement value")
    void shouldDecrementValue() {
         store.set("counter", "10");
         long result = store.decr("counter");
         assertThat(result).isEqualTo(9);
        
    }

    @Test
    @DisplayName("should decrement non-existent key from zero")
    void shouldDecrementFromZero() {
         long result = store.decr("counter");
         assertThat(result).isEqualTo(-1);
    }

    @Test
    @DisplayName("should throw when incrementing non-numeric value")
    void shouldThrowOnIncrNonNumeric() {
         store.set("key", "notanumber");
         assertThatThrownBy(() -> store.incr("key"))
                 .isInstanceOf(RuntimeException.class);
        
    }
}
