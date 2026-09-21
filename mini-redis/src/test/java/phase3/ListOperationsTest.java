package phase3;

import com.mohamed.halim.miniredis.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Tag("phase3")
@DisplayName("List Operations")
class ListOperationsTest {

    // TODO: Replace with your implementation
    // private DataStore store;

    // @BeforeEach
    // void setUp() {
    //     store = new MiniRedisDataStore();
    // }

    @Test
    @DisplayName("should push to left and report length")
    void shouldLpushAndReportLength() {
        // assertThat(store.lpush("list", "a")).isEqualTo(1);
        // assertThat(store.lpush("list", "b")).isEqualTo(2);
        // assertThat(store.lpush("list", "c")).isEqualTo(3);
        fail("Implement list operations and uncomment this test");
    }

    @Test
    @DisplayName("should push to right and report length")
    void shouldRpushAndReportLength() {
        // assertThat(store.rpush("list", "a")).isEqualTo(1);
        // assertThat(store.rpush("list", "b")).isEqualTo(2);
        fail("Implement list operations and uncomment this test");
    }

    @Test
    @DisplayName("should push multiple values at once")
    void shouldPushMultipleValues() {
        // assertThat(store.lpush("list", "a", "b", "c")).isEqualTo(3);
        fail("Implement list operations and uncomment this test");
    }

    @Test
    @DisplayName("should pop from left (LIFO for lpush)")
    void shouldLpop() {
        // store.lpush("list", "a", "b", "c");
        // assertThat(store.lpop("list")).isEqualTo("c");
        // assertThat(store.lpop("list")).isEqualTo("b");
        // assertThat(store.lpop("list")).isEqualTo("a");
        fail("Implement list operations and uncomment this test");
    }

    @Test
    @DisplayName("should pop from right")
    void shouldRpop() {
        // store.rpush("list", "a", "b", "c");
        // assertThat(store.rpop("list")).isEqualTo("c");
        // assertThat(store.rpop("list")).isEqualTo("b");
        fail("Implement list operations and uncomment this test");
    }

    @Test
    @DisplayName("should return null when popping from empty list")
    void shouldReturnNullOnEmptyPop() {
        // assertThat(store.lpop("nonexistent")).isNull();
        fail("Implement list operations and uncomment this test");
    }

    @Test
    @DisplayName("should return list length")
    void shouldReturnListLength() {
        // store.rpush("list", "a", "b", "c");
        // assertThat(store.llen("list")).isEqualTo(3);
        fail("Implement list operations and uncomment this test");
    }

    @Test
    @DisplayName("should return 0 length for non-existent list")
    void shouldReturnZeroLengthForNonExistent() {
        // assertThat(store.llen("nonexistent")).isEqualTo(0);
        fail("Implement list operations and uncomment this test");
    }

    @Test
    @DisplayName("should return range of elements")
    void shouldReturnRange() {
        // store.rpush("list", "a", "b", "c", "d", "e");
        // assertThat(store.lrange("list", 0, 2)).containsExactly("a", "b", "c");
        fail("Implement list operations and uncomment this test");
    }

    @Test
    @DisplayName("should handle negative indices in lrange")
    void shouldHandleNegativeIndices() {
        // store.rpush("list", "a", "b", "c", "d", "e");
        // assertThat(store.lrange("list", -3, -1)).containsExactly("c", "d", "e");
        fail("Implement list operations and uncomment this test");
    }

    @Test
    @DisplayName("should return full list with lrange 0 -1")
    void shouldReturnFullList() {
        // store.rpush("list", "a", "b", "c");
        // assertThat(store.lrange("list", 0, -1)).containsExactly("a", "b", "c");
        fail("Implement list operations and uncomment this test");
    }

    @Test
    @DisplayName("should throw WRONGTYPE when using list commands on string")
    void shouldThrowWrongTypeOnString() {
        // store.set("key", "value");
        // assertThatThrownBy(() -> store.lpush("key", "a"))
        //         .isInstanceOf(WrongTypeException.class);
        fail("Implement list operations and uncomment this test");
    }
}
