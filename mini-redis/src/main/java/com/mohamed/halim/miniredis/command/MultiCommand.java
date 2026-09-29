package com.mohamed.halim.miniredis.command;

import com.mohamed.halim.miniredis.resp.RespEncoder;
import com.mohamed.halim.miniredis.server.ClientContext;

import java.util.List;

public class MultiCommand implements Command {
    @Override
    public byte[] execute(List<String> command) {
        return new byte[0];
    }

    @Override
    public byte[] execute(List<String> command, ClientContext context) {
        context.setMode(ClientContext.ClientMode.TRANSACTION);
        return RespEncoder.getInstance().encodeSimpleString("OK");
    }
}
