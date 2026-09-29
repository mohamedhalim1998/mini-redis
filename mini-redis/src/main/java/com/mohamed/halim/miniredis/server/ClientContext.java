package com.mohamed.halim.miniredis.server;

import java.io.ByteArrayOutputStream;
import java.nio.channels.SelectionKey;
import java.util.ArrayList;
import java.util.List;

public class ClientContext {
    private ClientMode mode;
    private final String clientId;
    private final ByteArrayOutputStream output;
    private SelectionKey key;
    private final List<List<String>> queuedCommands;

    public ClientContext(String clientId) {
        this.clientId = clientId;
        this.mode = ClientMode.DEFAULT;
        this.output = new ByteArrayOutputStream();
        this.queuedCommands = new ArrayList<>();
    }

    public ByteArrayOutputStream output() {
        return output;
    }

    public String getClientId() {
        return clientId;
    }

    public ClientMode getMode() {
        return mode;
    }

    public void setMode(ClientMode mode) {
        this.mode = mode;
    }

    public SelectionKey getKey() {
        return key;
    }

    public void setKey(SelectionKey key) {
        this.key = key;
    }

    public List<List<String>> getQueuedCommands() {
        return queuedCommands;
    }

    public void addCommand(List<String> command) {
        queuedCommands.add(command);
    }

    public enum ClientMode {
        DEFAULT,
        TRANSACTION,
        PUB_SUB
    }
}
