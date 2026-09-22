package com.mohamed.halim.miniredis.command;

import com.mohamed.halim.miniredis.resp.RespEncoder;

import java.util.List;


public class PingCommand implements Command {
    private final RespEncoder encoder = RespEncoder.getInstance();
    @Override
    public byte[] execute(List<String> command) {
        return encoder.encodeSimpleString("PONG");
    }
}
