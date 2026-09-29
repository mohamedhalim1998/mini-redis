package com.mohamed.halim.miniredis.command;

import com.mohamed.halim.miniredis.server.ClientContext;

import java.util.List;

public interface Command {
    byte[] execute(List<String> command);

    default byte[] execute(List<String> command, ClientContext context) {
        return execute(command);
    }
}
