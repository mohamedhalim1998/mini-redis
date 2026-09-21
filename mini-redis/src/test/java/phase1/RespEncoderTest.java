package phase1;

import com.mohamed.halim.miniredis.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Tag("phase1")
@DisplayName("RESP Encoder")
class RespEncoderTest {

     private RespEncoder encoder;

     @BeforeEach
     void setUp() {
         encoder = new RespEncoderImpl();
     }

    private String decode(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8);
    }

    @Test
    @DisplayName("should encode simple string")
    void shouldEncodeSimpleString() {
      assertThat(decode(encoder.encodeSimpleString("OK"))).isEqualTo("+OK\r\n");
        
    }

    @Test
    @DisplayName("should encode PONG")
    void shouldEncodePong() {
      assertThat(decode(encoder.encodeSimpleString("PONG"))).isEqualTo("+PONG\r\n");
        
    }

    @Test
    @DisplayName("should encode error")
    void shouldEncodeError() {
      assertThat(decode(encoder.encodeError("ERR", "unknown command 'foo'")))
               .isEqualTo("-ERR unknown command 'foo'\r\n");
        
    }

    @Test
    @DisplayName("should encode integer")
    void shouldEncodeInteger() {
      assertThat(decode(encoder.encodeInteger(42))).isEqualTo(":42\r\n");
        
    }

    @Test
    @DisplayName("should encode negative integer")
    void shouldEncodeNegativeInteger() {
      assertThat(decode(encoder.encodeInteger(-1))).isEqualTo(":-1\r\n");
        
    }

    @Test
    @DisplayName("should encode bulk string")
    void shouldEncodeBulkString() {
      assertThat(decode(encoder.encodeBulkString("hello"))).isEqualTo("$5\r\nhello\r\n");
        
    }

    @Test
    @DisplayName("should encode null bulk string")
    void shouldEncodeNullBulkString() {
      assertThat(decode(encoder.encodeBulkString(null))).isEqualTo("$-1\r\n");
        
    }

    @Test
    @DisplayName("should encode empty bulk string")
    void shouldEncodeEmptyBulkString() {
      assertThat(decode(encoder.encodeBulkString(""))).isEqualTo("$0\r\n\r\n");
        
    }

    @Test
    @DisplayName("should encode array of bulk strings")
    void shouldEncodeArray() {
      assertThat(decode(encoder.encodeArray(List.of("foo", "bar"))))
                 .isEqualTo("*2\r\n$3\r\nfoo\r\n$3\r\nbar\r\n");
        
    }

    @Test
    @DisplayName("should encode null array")
    void shouldEncodeNullArray() {
      assertThat(decode(encoder.encodeArray(null))).isEqualTo("*-1\r\n");
        
    }

    @Test
    @DisplayName("should encode empty array")
    void shouldEncodeEmptyArray() {
      assertThat(decode(encoder.encodeArray(List.of()))).isEqualTo("*0\r\n");
        
    }
}
