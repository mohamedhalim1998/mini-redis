package com.mohamed.halim.miniredis.command;

import com.mohamed.halim.miniredis.datastore.DataStore;
import com.mohamed.halim.miniredis.resp.RespEncoder;

import java.util.List;

public class PresistCommand implements Command {
    @Override
    public byte[] execute(List<String> command) {
        if (command.size() != 2) {
            return RespEncoder.getInstance().encodeError(
                    "ERR",
                    "wrong number of arguments for 'PERSIST' command"
            );
        }
        var encoder = RespEncoder.getInstance();
        var store = DataStore.getInstance();
        return encoder.encodeInteger(store.persist(command.get(1)) ? 1 : 0);
    }
}
