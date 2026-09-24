package com.mohamed.halim.miniredis.command;

import com.mohamed.halim.miniredis.datastore.DataStore;
import com.mohamed.halim.miniredis.resp.RespEncoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HgetAllCommand implements Command {
    @Override
    public byte[] execute(List<String> command) {
        if(command.size() < 2) {
            return RespEncoder.getInstance().encodeError(
                    "ERR",
                    "wrong number of arguments for 'hgetall' command"
            );
        }
        var encoder = RespEncoder.getInstance();
        var store = DataStore.getInstance();
        var values = store.hgetall(command.get(1));
        var flatValues = new ArrayList<String>();
        values.forEach((k, v) -> Collections.addAll(flatValues, k , v));
        return encoder.encodeArray(flatValues);
    }
}
