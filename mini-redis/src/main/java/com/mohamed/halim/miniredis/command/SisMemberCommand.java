package com.mohamed.halim.miniredis.command;

import com.mohamed.halim.miniredis.datastore.DataStore;
import com.mohamed.halim.miniredis.resp.RespEncoder;

import java.util.List;

public class SisMemberCommand implements Command {
    @Override
    public byte[] execute(List<String> command) {
        if(command.size() < 3) {
            return RespEncoder.getInstance().encodeError(
                    "ERR",
                    "wrong number of arguments for 'srem' command"
            );
        }
        var encoder = RespEncoder.getInstance();
        var store = DataStore.getInstance();
        var isMember = store.sismember(command.get(1), command.get(2));
        return encoder.encodeInteger(isMember ? 1 : 0);
    }
}
