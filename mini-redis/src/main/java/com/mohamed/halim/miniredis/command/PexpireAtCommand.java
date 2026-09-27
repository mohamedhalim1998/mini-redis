package com.mohamed.halim.miniredis.command;

import com.mohamed.halim.miniredis.datastore.DataStore;
import com.mohamed.halim.miniredis.resp.RespEncoder;
import com.mohamed.halim.miniredis.utils.DataUtils;

import java.util.List;

public class PexpireAtCommand implements Command {
    @Override
    public byte[] execute(List<String> command) {
        if (command.size() < 3) {
            return RespEncoder.getInstance().encodeError(
                    "ERR",
                    "wrong number of arguments for 'pexpireat' command"
            );
        }
        var encoder = RespEncoder.getInstance();
        var store = DataStore.getInstance();
        var ttl = DataUtils.parse(command.get(2));
        var timeoutSet = store.expireAt(command.get(1), ttl);
        return encoder.encodeInteger(timeoutSet ? 1 : 0);
    }
}
