package com.mohamed.halim.miniredis.command;

import com.mohamed.halim.miniredis.datastore.DataStore;
import com.mohamed.halim.miniredis.resp.RespEncoder;

import java.util.List;

public class KeysCommand implements Command {
    @Override
    public byte[] execute(List<String> command) {
        if(command.size() < 2) {
            return RespEncoder.getInstance().encodeError(
                    "ERR",
                    "wrong number of arguments for 'get' command"
            );
        }
        var encoder = RespEncoder.getInstance();
        var store = DataStore.getInstance();
        var keys = store.keys(command.get(1));
        return encoder.encodeArray(keys);
    }
}
