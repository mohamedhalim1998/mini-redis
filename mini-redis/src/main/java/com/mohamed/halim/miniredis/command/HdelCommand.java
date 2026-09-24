package com.mohamed.halim.miniredis.command;

import com.mohamed.halim.miniredis.datastore.DataStore;
import com.mohamed.halim.miniredis.resp.RespEncoder;

import java.util.List;

public class HdelCommand implements Command {
    @Override
    public byte[] execute(List<String> command) {
        if(command.size() < 2) {
            return RespEncoder.getInstance().encodeError(
                    "ERR",
                    "wrong number of arguments for 'hdel' command"
            );
        }
        var encoder = RespEncoder.getInstance();
        var store = DataStore.getInstance();
        var values = command.stream().skip(2).toArray(String[]::new);
        var count = store.hdel(command.get(1), values);
        return encoder.encodeInteger(count);
    }
}
