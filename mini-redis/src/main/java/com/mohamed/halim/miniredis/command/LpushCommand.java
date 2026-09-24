package com.mohamed.halim.miniredis.command;

import com.mohamed.halim.miniredis.datastore.DataStore;
import com.mohamed.halim.miniredis.resp.RespEncoder;

import java.util.List;

public class LpushCommand implements Command {
    @Override
    public byte[] execute(List<String> command) {
        if(command.size() < 3) {
            return RespEncoder.getInstance().encodeError(
                    "ERR",
                    "wrong number of arguments for 'lpush' command"
            );
        }
        var encoder = RespEncoder.getInstance();
        var store = DataStore.getInstance();
        var values = command.stream().skip(2).toArray(String[]::new);
        var count = store.lpush(command.get(1), values);
        return encoder.encodeInteger(count);
    }
}
