package phase1;

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

@Tag("phase1")
@DisplayName("RESP Parser")
class RespParserTest {

    // TODO: Replace with your implementation
    // private RespParser parser;

    // @BeforeEach
    // void setUp() {
    //     parser = new MiniRedisRespParser();
    // }

    private InputStream input(String data) {
        return new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    @DisplayName("should parse simple string")
    void shouldParseSimpleString() {
        // RespValue result = parser.parse(input("+OK\r\n"));
        // assertThat(result).isInstanceOf(RespValue.SimpleString.class);
        // assertThat(((RespValue.SimpleString) result).value()).isEqualTo("OK");
        fail("Implement RespParser and uncomment this test");
    }

    @Test
    @DisplayName("should parse error")
    void shouldParseError() {
        // RespValue result = parser.parse(input("-ERR unknown command\r\n"));
        // assertThat(result).isInstanceOf(RespValue.Error.class);
        // var error = (RespValue.Error) result;
        // assertThat(error.type()).isEqualTo("ERR");
        // assertThat(error.message()).isEqualTo("unknown command");
        fail("Implement RespParser and uncomment this test");
    }

    @Test
    @DisplayName("should parse integer")
    void shouldParseInteger() {
        // RespValue result = parser.parse(input(":1000\r\n"));
        // assertThat(result).isInstanceOf(RespValue.Integer.class);
        // assertThat(((RespValue.Integer) result).value()).isEqualTo(1000L);
        fail("Implement RespParser and uncomment this test");
    }

    @Test
    @DisplayName("should parse negative integer")
    void shouldParseNegativeInteger() {
        // RespValue result = parser.parse(input(":-42\r\n"));
        // assertThat(result).isInstanceOf(RespValue.Integer.class);
        // assertThat(((RespValue.Integer) result).value()).isEqualTo(-42L);
        fail("Implement RespParser and uncomment this test");
    }

    @Test
    @DisplayName("should parse bulk string")
    void shouldParseBulkString() {
        // RespValue result = parser.parse(input("$5\r\nhello\r\n"));
        // assertThat(result).isInstanceOf(RespValue.BulkString.class);
        // assertThat(((RespValue.BulkString) result).value()).isEqualTo("hello");
        fail("Implement RespParser and uncomment this test");
    }

    @Test
    @DisplayName("should parse null bulk string")
    void shouldParseNullBulkString() {
        // RespValue result = parser.parse(input("$-1\r\n"));
        // assertThat(result).isInstanceOf(RespValue.BulkString.class);
        // assertThat(((RespValue.BulkString) result).isNull()).isTrue();
        fail("Implement RespParser and uncomment this test");
    }

    @Test
    @DisplayName("should parse empty bulk string")
    void shouldParseEmptyBulkString() {
        // RespValue result = parser.parse(input("$0\r\n\r\n"));
        // assertThat(result).isInstanceOf(RespValue.BulkString.class);
        // assertThat(((RespValue.BulkString) result).value()).isEqualTo("");
        fail("Implement RespParser and uncomment this test");
    }

    @Test
    @DisplayName("should parse array")
    void shouldParseArray() {
        // RespValue result = parser.parse(input("*2\r\n$3\r\nfoo\r\n$3\r\nbar\r\n"));
        // assertThat(result).isInstanceOf(RespValue.Array.class);
        // var array = (RespValue.Array) result;
        // assertThat(array.elements()).hasSize(2);
        // assertThat(((RespValue.BulkString) array.elements().get(0)).value()).isEqualTo("foo");
        // assertThat(((RespValue.BulkString) array.elements().get(1)).value()).isEqualTo("bar");
        fail("Implement RespParser and uncomment this test");
    }

    @Test
    @DisplayName("should parse command: SET key value")
    void shouldParseSetCommand() {
        // List<String> command = parser.parseCommand(input("*3\r\n$3\r\nSET\r\n$5\r\nmykey\r\n$7\r\nmyvalue\r\n"));
        // assertThat(command).containsExactly("SET", "mykey", "myvalue");
        fail("Implement RespParser and uncomment this test");
    }

    @Test
    @DisplayName("should parse command: GET key")
    void shouldParseGetCommand() {
        // List<String> command = parser.parseCommand(input("*2\r\n$3\r\nGET\r\n$5\r\nmykey\r\n"));
        // assertThat(command).containsExactly("GET", "mykey");
        fail("Implement RespParser and uncomment this test");
    }

    @Test
    @DisplayName("should throw on malformed input")
    void shouldThrowOnMalformedInput() {
        // assertThatThrownBy(() -> parser.parse(input("invalid\r\n")))
        //         .isInstanceOf(RespParseException.class);
        fail("Implement RespParser and uncomment this test");
    }
}
