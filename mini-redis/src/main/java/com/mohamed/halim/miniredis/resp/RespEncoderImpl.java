package com.mohamed.halim.miniredis.resp;

import java.util.List;

public class RespEncoderImpl implements RespEncoder {
    public static final String END_LINE = "\r\n";
    @Override
    public byte[] encodeSimpleString(String value) {
        String builder = RespType.SIMPLE.getPrefix() +
                value +
                END_LINE;
        return builder.getBytes();
    }

    @Override
    public byte[] encodeError(String errorType, String message) {
        String builder = RespType.ERROR.getPrefix() +
                errorType + " " + message +
                END_LINE;
        return builder.getBytes();
    }

    @Override
    public byte[] encodeInteger(long value) {
        String builder = RespType.INTEGER.getPrefix() +
                String.valueOf(value) +
                END_LINE;
        return builder.getBytes();
    }

    @Override
    public byte[] encodeBulkString(String value) {
        if (value == null) {
            return "$-1\r\n".getBytes();
        }
        String builder = encodeString(value);
        return builder.getBytes();
    }

    private String encodeString(String value) {
        return (String.valueOf(RespType.BULK_STRING.getPrefix()))
                .concat(String.valueOf(value.length()))
                .concat(END_LINE)
                .concat(value)
                .concat(END_LINE);
    }

    @Override
    public byte[] encodeArray(List<String> elements) {
        if (elements == null) {
            return "*-1\r\n".getBytes();
        }
        StringBuilder builder = new StringBuilder();
        builder.append(RespType.ARRAY.getPrefix())
                .append(elements.size())
                .append(END_LINE);
        elements.stream().map(this::encodeString).forEach(builder::append);
        return builder.toString().getBytes();
    }

    @Override
    public byte[] encodePubSubMessage(String channel, String message) {
        return new byte[0];
    }
}
