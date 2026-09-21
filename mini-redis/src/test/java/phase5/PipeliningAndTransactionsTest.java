package phase5;

import com.mohamed.halim.miniredis.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Tag("phase5")
@DisplayName("Pipelining & Transactions")
class PipeliningAndTransactionsTest {

    // TODO: Replace with your implementations
    // private DataStore store;
    // private CommandExecutor executor;

    // @BeforeEach
    // void setUp() {
    //     store = new MiniRedisDataStore();
    //     executor = new MiniRedisCommandExecutor(store);
    // }

    private InputStream input(String data) {
        return new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    @DisplayName("should execute pipelined commands and return ordered responses")
    void shouldExecutePipelinedCommands() {
        // // Simulate pipeline: SET key1 val1, SET key2 val2, GET key1, GET key2
        // byte[] resp1 = executor.execute(List.of("SET", "key1", "val1"));
        // byte[] resp2 = executor.execute(List.of("SET", "key2", "val2"));
        // byte[] resp3 = executor.execute(List.of("GET", "key1"));
        // byte[] resp4 = executor.execute(List.of("GET", "key2"));
        //
        // String r1 = new String(resp1, StandardCharsets.UTF_8);
        // String r2 = new String(resp2, StandardCharsets.UTF_8);
        // String r3 = new String(resp3, StandardCharsets.UTF_8);
        // String r4 = new String(resp4, StandardCharsets.UTF_8);
        //
        // assertThat(r1).isEqualTo("+OK\r\n");
        // assertThat(r2).isEqualTo("+OK\r\n");
        // assertThat(r3).isEqualTo("$4\r\nval1\r\n");
        // assertThat(r4).isEqualTo("$4\r\nval2\r\n");
        fail("Implement CommandExecutor and uncomment this test");
    }

    @Test
    @DisplayName("MULTI should queue commands and EXEC should execute atomically")
    void shouldQueueAndExecTransaction() {
        // // MULTI
        // byte[] multiResp = executor.execute(List.of("MULTI"));
        // assertThat(new String(multiResp, StandardCharsets.UTF_8)).isEqualTo("+OK\r\n");
        //
        // // Commands should be queued (return +QUEUED)
        // byte[] q1 = executor.execute(List.of("SET", "tx-key", "tx-value"));
        // byte[] q2 = executor.execute(List.of("INCR", "counter"));
        // assertThat(new String(q1, StandardCharsets.UTF_8)).isEqualTo("+QUEUED\r\n");
        // assertThat(new String(q2, StandardCharsets.UTF_8)).isEqualTo("+QUEUED\r\n");
        //
        // // EXEC should execute all and return array of results
        // byte[] execResp = executor.execute(List.of("EXEC"));
        // String execStr = new String(execResp, StandardCharsets.UTF_8);
        // assertThat(execStr).startsWith("*2\r\n"); // array of 2 results
        //
        // // Verify state
        // assertThat(store.get("tx-key")).isEqualTo("tx-value");
        // assertThat(store.get("counter")).isEqualTo("1");
        fail("Implement transaction support and uncomment this test");
    }

    @Test
    @DisplayName("DISCARD should cancel transaction")
    void shouldDiscardTransaction() {
        // executor.execute(List.of("MULTI"));
        // executor.execute(List.of("SET", "key", "value"));
        // byte[] discardResp = executor.execute(List.of("DISCARD"));
        //
        // assertThat(new String(discardResp, StandardCharsets.UTF_8)).isEqualTo("+OK\r\n");
        // assertThat(store.get("key")).isNull();
        fail("Implement transaction support and uncomment this test");
    }

    @Test
    @DisplayName("should return error for EXEC without MULTI")
    void shouldErrorOnExecWithoutMulti() {
        // byte[] resp = executor.execute(List.of("EXEC"));
        // String str = new String(resp, StandardCharsets.UTF_8);
        // assertThat(str).startsWith("-ERR");
        fail("Implement transaction support and uncomment this test");
    }

    @Test
    @DisplayName("should return error for DISCARD without MULTI")
    void shouldErrorOnDiscardWithoutMulti() {
        // byte[] resp = executor.execute(List.of("DISCARD"));
        // String str = new String(resp, StandardCharsets.UTF_8);
        // assertThat(str).startsWith("-ERR");
        fail("Implement transaction support and uncomment this test");
    }
}
