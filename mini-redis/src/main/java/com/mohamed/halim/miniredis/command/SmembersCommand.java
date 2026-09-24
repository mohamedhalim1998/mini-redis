package com.mohamed.halim.miniredis.command;

import com.mohamed.halim.miniredis.datastore.DataStore;
import com.mohamed.halim.miniredis.resp.RespEncoder;
import com.mohamed.halim.miniredis.utils.DataUtils;

import java.util.ArrayList;
import java.util.List;

public class SmembersCommand implements Command {
    @Override
    public byte[] execute(List<String> command) {
        if(command.size() != 2) {
            return RespEncoder.getInstance().encodeError(
                    "ERR",
                    "wrong number of arguments for 'smembers' command"
            );
        }
        var encoder = RespEncoder.getInstance();
        var store = DataStore.getInstance();
        var values = store.smembers(command.get(1));
        return encoder.encodeArray(values);
    }
}
