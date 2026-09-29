package com.mohamed.halim.miniredis.command;

import com.mohamed.halim.miniredis.pubsub.PubSub;
import com.mohamed.halim.miniredis.resp.RespEncoder;
import com.mohamed.halim.miniredis.server.ClientContext;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.util.List;
import java.util.function.BiConsumer;

public class SubscribeCommand implements Command {
    @Override
    public byte[] execute(List<String> command) {
        return new byte[0];
    }

    @Override
    public byte[] execute(List<String> command, ClientContext context) {
        context.setMode(ClientContext.ClientMode.PUB_SUB);
        var channels = command.stream().skip(1).toArray(String[]::new);
        var listener = new BiConsumer<String, String>() {
            @Override
            public void accept(String channel, String message) {
                try {
                    context.output().write(RespEncoder.getInstance().encodePubSubMessage(channel, message));
                    context.getKey().interestOps(SelectionKey.OP_WRITE | SelectionKey.OP_READ);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        var count = PubSub.getInstance().subscribe(context.getClientId(), listener, channels);
        return RespEncoder.getInstance().encodeInteger(count);
    }
}
