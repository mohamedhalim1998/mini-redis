package com.mohamed.halim.miniredis.command;

import com.mohamed.halim.miniredis.pubsub.PubSub;
import com.mohamed.halim.miniredis.resp.RespEncoder;
import com.mohamed.halim.miniredis.server.ClientContext;

import java.util.List;

public class PublishCommand implements Command {
    @Override
    public byte[] execute(List<String> command) {
        return new byte[0];
    }

    @Override
    public byte[] execute(List<String> command, ClientContext context) {
        var count = PubSub.getInstance().publish(command.get(1), command.get(2));
        return RespEncoder.getInstance().encodeInteger(count);
    }
}
