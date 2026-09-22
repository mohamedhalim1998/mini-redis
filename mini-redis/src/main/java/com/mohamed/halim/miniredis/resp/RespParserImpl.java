package com.mohamed.halim.miniredis.resp;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.IntStream;

public class RespParserImpl implements RespParser {
    @Override
    public RespValue parse(InputStream input) {
        try {
            var token = input.readNBytes(1);
            char prefix = (char) token[0];
            return parse(input, RespType.fromPrefix(prefix));
        } catch (IOException | IllegalArgumentException e) {
            throw new RespParseException(e.getMessage());
        }
    }

    private RespValue parse(InputStream input, RespType respType) throws IOException {
        return switch (respType) {
            case SIMPLE -> new SimpleString(readUntilNewLine(input));
            case ERROR -> new Error(readUntilNewLine(input));
            case INTEGER -> parseInteger(input);
            case BULK_STRING -> parseBulkString(input);
            case ARRAY -> parseArray(input);
        };
    }

    private RespValue parseArray(InputStream input) throws IOException {
        var length = readNumber(input).intValue();
        return new Array(
                IntStream.range(0, length)
                        .mapToObj(_ -> parse(input))
                        .toList()
        );
    }

    private RespValue parseBulkString(InputStream input) throws IOException {
        var length = readNumber(input).intValue();
        if (length == -1) {
            return new BulkString(null);
        }
        var value = input.readNBytes(length + 2);
        return new BulkString(new String(value).trim());
    }

    private RespValue parseInteger(InputStream input) throws IOException {
        return new Integer(readNumber(input));
    }

    private Long readNumber(InputStream input) throws IOException {
        var number = readUntilNewLine(input);
        try {
            return Long.parseLong(number);
        } catch (Exception e) {
            throw new RespParseException(e.getMessage());
        }
    }


    private String readUntilNewLine(InputStream input) throws IOException {
        var builder = new StringBuilder();
        var next = input.read();
        do {
            builder.append((char) next);
            next = input.read();
        } while (next != '\r');
        next = input.read();// skipping \n
        if (next != '\n') {
            throw new RespParseException("mis formated message");
        }
        return builder.toString();
    }

    private static InputStream getInput(InputStream input) {
        return input;
    }

    @Override
    public List<String> parseCommand(InputStream input) {
        var command = parse(input);
        if(!RespType.ARRAY.equals(command.type())) {
            throw new RespParseException("command should be array");
        }
        return ((Array) command).elements().stream().map(element -> {
            if(!RespType.BULK_STRING.equals(element.type())) {
                throw new RespParseException("command parts should be strings");
            } else {
                return ((BulkString) element).value();
            }
        }).toList();
    }
}
