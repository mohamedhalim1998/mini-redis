package com.mohamed.halim.miniredis.command;

import java.util.List;

public interface Command {
    byte[] execute(List<String> command);
}
