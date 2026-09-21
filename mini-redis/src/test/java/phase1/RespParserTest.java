package phase1;

import com.mohamed.halim.miniredis.*;
import com.mohamed.halim.miniredis.resp.RespValue;
import com.mohamed.halim.miniredis.resp.*;
import com.mohamed.halim.miniredis.resp.Integer;
import com.mohamed.halim.miniredis.resp.Error;
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

     private RespParser parser;

     @BeforeEach
     void setUp() {
         parser = new RespParserImpl();
     }

    private InputStream input(String data) {
        return new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    @DisplayName("should parse simple string")
    void shouldParseSimpleString() {
         RespValue result = parser.parse(input("+OK\r\n"));
         assertThat(result).isInstanceOf(SimpleString.class);
         assertThat(((SimpleString) result).value()).isEqualTo("OK");
    }

    @Test
    @DisplayName("should parse error")
    void shouldParseError() {
         RespValue result = parser.parse(input("-ERR unknown command\r\n"));
         assertThat(result).isInstanceOf(Error.class);
         var error = (Error) result;
         assertThat(error.message()).isEqualTo("ERR unknown command");

    }

    @Test
    @DisplayName("should parse integer")
    void shouldParseInteger() {
         RespValue result = parser.parse(input(":1000\r\n"));
         assertThat(result).isInstanceOf(Integer.class);
         assertThat(((Integer) result).value()).isEqualTo(1000L);
    }

    @Test
    @DisplayName("should parse negative integer")
    void shouldParseNegativeInteger() {
         RespValue result = parser.parse(input(":-42\r\n"));
         assertThat(result).isInstanceOf(Integer.class);
         assertThat(((Integer) result).value()).isEqualTo(-42L);
    }

    @Test
    @DisplayName("should parse bulk string")
    void shouldParseBulkString() {
         RespValue result = parser.parse(input("$5\r\nhello\r\n"));
         assertThat(result).isInstanceOf(BulkString.class);
         assertThat(((BulkString) result).value()).isEqualTo("hello");
    }

    @Test
    @DisplayName("should parse null bulk string")
    void shouldParseNullBulkString() {
         RespValue result = parser.parse(input("$-1\r\n"));
         assertThat(result).isInstanceOf(BulkString.class);
         assertThat(((BulkString) result).isNull()).isTrue();
    }

    @Test
    @DisplayName("should parse empty bulk string")
    void shouldParseEmptyBulkString() {
         RespValue result = parser.parse(input("$0\r\n\r\n"));
         assertThat(result).isInstanceOf(BulkString.class);
         assertThat(((BulkString) result).value()).isEqualTo("");
    }

    @Test
    @DisplayName("should parse array")
    void shouldParseArray() {
         RespValue result = parser.parse(input("*2\r\n$3\r\nfoo\r\n$3\r\nbar\r\n"));
         assertThat(result).isInstanceOf(Array.class);
         var array = (Array) result;
         assertThat(array.elements()).hasSize(2);
         assertThat(((BulkString) array.elements().get(0)).value()).isEqualTo("foo");
         assertThat(((BulkString) array.elements().get(1)).value()).isEqualTo("bar");
    }

    @Test
    @DisplayName("should parse command: SET key value")
    void shouldParseSetCommand() {
         List<String> command = parser.parseCommand(input("*3\r\n$3\r\nSET\r\n$5\r\nmykey\r\n$7\r\nmyvalue\r\n"));
         assertThat(command).containsExactly("SET", "mykey", "myvalue");
    }

    @Test
    @DisplayName("should parse command: GET key")
    void shouldParseGetCommand() {
         List<String> command = parser.parseCommand(input("*2\r\n$3\r\nGET\r\n$5\r\nmykey\r\n"));
         assertThat(command).containsExactly("GET", "mykey");
    }

    @Test
    @DisplayName("should throw on malformed input")
    void shouldThrowOnMalformedInput() {
         assertThatThrownBy(() -> parser.parse(input("invalid\r\n")))
                 .isInstanceOf(RespParseException.class);
    }
}
