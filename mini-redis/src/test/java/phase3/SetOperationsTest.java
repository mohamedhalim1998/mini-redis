package phase3;

import com.mohamed.halim.miniredis.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@Tag("phase3")
@DisplayName("Set Operations")
class SetOperationsTest {

    // TODO: Replace with your implementation
    // private DataStore store;

    // @BeforeEach
    // void setUp() {
    //     store = new MiniRedisDataStore();
    // }

    @Test
    @DisplayName("should add members and report count of new additions")
    void shouldAddMembers() {
        // assertThat(store.sadd("set", "a", "b", "c")).isEqualTo(3);
        // assertThat(store.sadd("set", "a", "d")).isEqualTo(1); // only "d" is new
        fail("Implement set operations and uncomment this test");
    }

    @Test
    @DisplayName("should remove members and report count of removals")
    void shouldRemoveMembers() {
        // store.sadd("set", "a", "b", "c");
        // assertThat(store.srem("set", "a", "nonexistent")).isEqualTo(1);
        fail("Implement set operations and uncomment this test");
    }

    @Test
    @DisplayName("should check membership")
    void shouldCheckMembership() {
        // store.sadd("set", "a", "b");
        // assertThat(store.sismember("set", "a")).isTrue();
        // assertThat(store.sismember("set", "z")).isFalse();
        fail("Implement set operations and uncomment this test");
    }

    @Test
    @DisplayName("should return false for membership in non-existent set")
    void shouldReturnFalseForNonExistentSet() {
        // assertThat(store.sismember("nonexistent", "a")).isFalse();
        fail("Implement set operations and uncomment this test");
    }

    @Test
    @DisplayName("should return all members")
    void shouldReturnAllMembers() {
        // store.sadd("set", "a", "b", "c");
        // assertThat(store.smembers("set")).containsExactlyInAnyOrder("a", "b", "c");
        fail("Implement set operations and uncomment this test");
    }

    @Test
    @DisplayName("should return empty set for non-existent key")
    void shouldReturnEmptySetForNonExistent() {
        // assertThat(store.smembers("nonexistent")).isEmpty();
        fail("Implement set operations and uncomment this test");
    }

    @Test
    @DisplayName("should return cardinality")
    void shouldReturnCardinality() {
        // store.sadd("set", "a", "b", "c");
        // assertThat(store.scard("set")).isEqualTo(3);
        fail("Implement set operations and uncomment this test");
    }

    @Test
    @DisplayName("should throw WRONGTYPE when using set commands on string")
    void shouldThrowWrongTypeOnString() {
        // store.set("key", "value");
        // assertThatThrownBy(() -> store.sadd("key", "a"))
        //         .isInstanceOf(WrongTypeException.class);
        fail("Implement set operations and uncomment this test");
    }
}
