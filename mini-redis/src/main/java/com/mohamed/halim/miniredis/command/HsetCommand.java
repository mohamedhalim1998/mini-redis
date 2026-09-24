package com.mohamed.halim.miniredis.command;

import com.mohamed.halim.miniredis.datastore.DataStore;
import com.mohamed.halim.miniredis.resp.RespEncoder;

import java.util.List;

public class HsetCommand implements Command {
    @Override
    public byte[] execute(List<String> command) {
        if(command.size() < 3) {
            return RespEncoder.getInstance().encodeError(
                    "ERR",
                    "wrong number of arguments for 'hset' command"
            );
        }
        var encoder = RespEncoder.getInstance();
        var store = DataStore.getInstance();
        var field = command.get(2);
        var value = command.get(3);
        var added = store.hset(command.get(1), field, value);
        return encoder.encodeInteger(added ? 1 : 0);
    }
}
