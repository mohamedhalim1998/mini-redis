package com.mohamed.halim.miniredis.command;

import com.mohamed.halim.miniredis.datastore.DataStore;
import com.mohamed.halim.miniredis.resp.RespEncoder;

import java.util.List;

public class ScardCommand implements Command {
    @Override
    public byte[] execute(List<String> command) {
        if(command.size() != 2) {
            return RespEncoder.getInstance().encodeError(
                    "ERR",
                    "wrong number of arguments for 'scard' command"
            );
        }
        var encoder = RespEncoder.getInstance();
        var store = DataStore.getInstance();
        var count = store.scard(command.get(1));
        return encoder.encodeInteger(count);
    }
}
