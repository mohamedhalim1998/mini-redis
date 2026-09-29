package com.mohamed.halim.miniredis.command;

import com.mohamed.halim.miniredis.pubsub.PubSub;
import com.mohamed.halim.miniredis.resp.RespEncoder;
import com.mohamed.halim.miniredis.server.ClientContext;

import java.util.List;

public class UnsubscribeCommand implements Command {
    @Override
    public byte[] execute(List<String> command) {
        return new byte[0];
    }

    @Override
    public byte[] execute(List<String> command, ClientContext context) {
        var channels = command.stream().skip(1).toArray(String[]::new);
        var count = PubSub.getInstance().unsubscribe(context.getClientId(), channels);
        return RespEncoder.getInstance().encodeInteger(count);
    }
}
