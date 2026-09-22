package com.mohamed.halim.miniredis.command;

import com.mohamed.halim.miniredis.datastore.DataStore;
import com.mohamed.halim.miniredis.resp.RespEncoder;

import java.util.ArrayList;
import java.util.List;

public class ExistCommand implements Command {
    @Override
    public byte[] execute(List<String> command) {
        if(command.size() < 2) {
            return RespEncoder.getInstance().encodeError(
                    "ERR",
                    "wrong number of arguments for 'EXISTS' command"
            );
        }
        var encoder = RespEncoder.getInstance();
        var store = DataStore.getInstance();
        var keys = new ArrayList<>(command);
        keys.removeFirst();
        int count = store.exists(keys.toArray(String[]::new));
        return encoder.encodeInteger(count);
    }
}
