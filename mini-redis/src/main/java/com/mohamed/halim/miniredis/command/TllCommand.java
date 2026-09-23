package com.mohamed.halim.miniredis.command;

import com.mohamed.halim.miniredis.datastore.DataStore;
import com.mohamed.halim.miniredis.resp.RespEncoder;

import java.util.List;

public class TllCommand implements Command {
    @Override
    public byte[] execute(List<String> command) {
        if (command.size() != 2) {
            return RespEncoder.getInstance().encodeError(
                    "ERR",
                    "wrong number of arguments for 'ttl' command"
            );
        }
        var encoder = RespEncoder.getInstance();
        var store = DataStore.getInstance();
        var ttl = store.pttl(command.get(1));
        if(ttl < 0) {
            return encoder.encodeInteger(ttl);
        }
        return encoder.encodeInteger(ttl / 1000);
    }

}
