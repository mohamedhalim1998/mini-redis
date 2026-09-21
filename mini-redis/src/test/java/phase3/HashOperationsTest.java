package phase3;

import com.mohamed.halim.miniredis.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@Tag("phase3")
@DisplayName("Hash Operations")
class HashOperationsTest {

    // TODO: Replace with your implementation
    // private DataStore store;

    // @BeforeEach
    // void setUp() {
    //     store = new MiniRedisDataStore();
    // }

    @Test
    @DisplayName("should set and get hash field")
    void shouldSetAndGetField() {
        // assertThat(store.hset("hash", "field1", "value1")).isTrue();
        // assertThat(store.hget("hash", "field1")).isEqualTo("value1");
        fail("Implement hash operations and uncomment this test");
    }

    @Test
    @DisplayName("should return false when overwriting existing field")
    void shouldReturnFalseOnOverwrite() {
        // store.hset("hash", "field1", "value1");
        // assertThat(store.hset("hash", "field1", "value2")).isFalse();
        // assertThat(store.hget("hash", "field1")).isEqualTo("value2");
        fail("Implement hash operations and uncomment this test");
    }

    @Test
    @DisplayName("should return null for non-existent field")
    void shouldReturnNullForMissingField() {
        // store.hset("hash", "field1", "value1");
        // assertThat(store.hget("hash", "nonexistent")).isNull();
        fail("Implement hash operations and uncomment this test");
    }

    @Test
    @DisplayName("should return null for non-existent hash key")
    void shouldReturnNullForMissingKey() {
        // assertThat(store.hget("nonexistent", "field")).isNull();
        fail("Implement hash operations and uncomment this test");
    }

    @Test
    @DisplayName("should delete hash fields")
    void shouldDeleteFields() {
        // store.hset("hash", "a", "1");
        // store.hset("hash", "b", "2");
        // store.hset("hash", "c", "3");
        // assertThat(store.hdel("hash", "a", "b", "nonexistent")).isEqualTo(2);
        fail("Implement hash operations and uncomment this test");
    }

    @Test
    @DisplayName("should get all fields and values")
    void shouldGetAll() {
        // store.hset("hash", "a", "1");
        // store.hset("hash", "b", "2");
        // assertThat(store.hgetall("hash"))
        //         .containsExactlyInAnyOrderEntriesOf(Map.of("a", "1", "b", "2"));
        fail("Implement hash operations and uncomment this test");
    }

    @Test
    @DisplayName("should return empty map for non-existent hash")
    void shouldReturnEmptyMapForNonExistent() {
        // assertThat(store.hgetall("nonexistent")).isEmpty();
        fail("Implement hash operations and uncomment this test");
    }

    @Test
    @DisplayName("should check field existence")
    void shouldCheckFieldExistence() {
        // store.hset("hash", "field1", "value1");
        // assertThat(store.hexists("hash", "field1")).isTrue();
        // assertThat(store.hexists("hash", "nonexistent")).isFalse();
        fail("Implement hash operations and uncomment this test");
    }

    @Test
    @DisplayName("should return hash length")
    void shouldReturnHashLength() {
        // store.hset("hash", "a", "1");
        // store.hset("hash", "b", "2");
        // store.hset("hash", "c", "3");
        // assertThat(store.hlen("hash")).isEqualTo(3);
        fail("Implement hash operations and uncomment this test");
    }

    @Test
    @DisplayName("should throw WRONGTYPE when using hash commands on string")
    void shouldThrowWrongTypeOnString() {
        // store.set("key", "value");
        // assertThatThrownBy(() -> store.hset("key", "field", "value"))
        //         .isInstanceOf(WrongTypeException.class);
        fail("Implement hash operations and uncomment this test");
    }
}
