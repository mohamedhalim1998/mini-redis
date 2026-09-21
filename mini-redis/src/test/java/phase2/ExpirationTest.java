package phase2;

import com.mohamed.halim.miniredis.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@Tag("phase2")
@DisplayName("Expiration & TTL")
class ExpirationTest {

    // TODO: Replace with your implementation
    // private DataStore store;

    // @BeforeEach
    // void setUp() {
    //     store = new MiniRedisDataStore();
    // }

    @Test
    @DisplayName("should store value with TTL and retrieve before expiry")
    void shouldRetrieveBeforeExpiry() {
        // store.setWithTtl("key", "value", 5000L);
        // assertThat(store.get("key")).isEqualTo("value");
        fail("Implement DataStore TTL support and uncomment this test");
    }

    @Test
    @DisplayName("should return null after TTL expires (lazy expiration)")
    void shouldReturnNullAfterExpiry() throws InterruptedException {
        // store.setWithTtl("key", "value", 50L);
        // Thread.sleep(100);
        // assertThat(store.get("key")).isNull();
        fail("Implement DataStore TTL support and uncomment this test");
    }

    @Test
    @DisplayName("should set expiration on existing key")
    void shouldSetExpirationOnExistingKey() throws InterruptedException {
        // store.set("key", "value");
        // boolean result = store.expire("key", 50L);
        // assertThat(result).isTrue();
        // Thread.sleep(100);
        // assertThat(store.get("key")).isNull();
        fail("Implement DataStore TTL support and uncomment this test");
    }

    @Test
    @DisplayName("should return false when setting expiry on non-existent key")
    void shouldReturnFalseForNonExistentExpire() {
        // assertThat(store.expire("nonexistent", 1000L)).isFalse();
        fail("Implement DataStore TTL support and uncomment this test");
    }

    @Test
    @DisplayName("should report TTL for key with expiration")
    void shouldReportTtl() {
        // store.setWithTtl("key", "value", 5000L);
        // long ttl = store.pttl("key");
        // assertThat(ttl).isBetween(4000L, 5000L);
        fail("Implement DataStore TTL support and uncomment this test");
    }

    @Test
    @DisplayName("should return -1 for key without expiration")
    void shouldReturnMinusOneForNoExpiry() {
        // store.set("key", "value");
        // assertThat(store.pttl("key")).isEqualTo(-1L);
        fail("Implement DataStore TTL support and uncomment this test");
    }

    @Test
    @DisplayName("should return -2 for non-existent key")
    void shouldReturnMinusTwoForNonExistent() {
        // assertThat(store.pttl("nonexistent")).isEqualTo(-2L);
        fail("Implement DataStore TTL support and uncomment this test");
    }

    @Test
    @DisplayName("should remove expiration with persist")
    void shouldRemoveExpirationWithPersist() throws InterruptedException {
        // store.setWithTtl("key", "value", 50L);
        // boolean result = store.persist("key");
        // assertThat(result).isTrue();
        // Thread.sleep(100);
        // assertThat(store.get("key")).isEqualTo("value");
        fail("Implement DataStore TTL support and uncomment this test");
    }

    @Test
    @DisplayName("should return false when persist on key without TTL")
    void shouldReturnFalseWhenPersistNoTtl() {
        // store.set("key", "value");
        // assertThat(store.persist("key")).isFalse();
        fail("Implement DataStore TTL support and uncomment this test");
    }

    @Test
    @DisplayName("should overwrite TTL when SET is called again")
    void shouldOverwriteTtlOnSet() {
        // store.setWithTtl("key", "value", 50L);
        // store.set("key", "newvalue");
        // assertThat(store.pttl("key")).isEqualTo(-1L);
        // assertThat(store.get("key")).isEqualTo("newvalue");
        fail("Implement DataStore TTL support and uncomment this test");
    }

    @Test
    @DisplayName("should expire keys during active expiration cycle")
    void shouldExpireKeysInActiveCycle() throws InterruptedException {
        // store.setWithTtl("a", "1", 50L);
        // store.setWithTtl("b", "2", 50L);
        // store.setWithTtl("c", "3", 50L);
        // Thread.sleep(100);
        // int expired = store.expireActiveCycle();
        // assertThat(expired).isGreaterThanOrEqualTo(1);
        fail("Implement DataStore TTL support and uncomment this test");
    }
}
