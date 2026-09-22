package com.mohamed.halim.miniredis.server;

import java.io.ByteArrayOutputStream;

public class ClientContext {
    private String input;
    private final ByteArrayOutputStream output = new ByteArrayOutputStream();

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public ByteArrayOutputStream output() {
        return output;
    }
}
