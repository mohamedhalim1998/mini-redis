package com.mohamed.halim.miniredis.command;

import com.mohamed.halim.miniredis.datastore.DataStore;
import com.mohamed.halim.miniredis.resp.RespEncoder;
import com.mohamed.halim.miniredis.utils.DataUtils;

import java.util.List;

public class LrangeCommand implements Command {
    @Override
    public byte[] execute(List<String> command) {
        if(command.size() != 4) {
            return RespEncoder.getInstance().encodeError(
                    "ERR",
                    "wrong number of arguments for 'lrange' command"
            );
        }
        var encoder = RespEncoder.getInstance();
        var store = DataStore.getInstance();
        var start = DataUtils.parse(command.get(2));
        var stop = DataUtils.parse(command.get(3));
        var values = store.lrange(command.get(1), start, stop);
        return encoder.encodeArray(values);
    }
}
