package com.mohamed.halim.miniredis.command;

import com.mohamed.halim.miniredis.datastore.DataStore;
import com.mohamed.halim.miniredis.resp.RespEncoder;

import java.util.List;

public class HgetCommand implements Command {
    @Override
    public byte[] execute(List<String> command) {
        if(command.size() < 2) {
            return RespEncoder.getInstance().encodeError(
                    "ERR",
                    "wrong number of arguments for 'hget' command"
            );
        }
        var encoder = RespEncoder.getInstance();
        var store = DataStore.getInstance();
        var field = command.get(2);
        var value = store.hget(command.get(1), field);
        return encoder.encodeBulkString(value);
    }
}
