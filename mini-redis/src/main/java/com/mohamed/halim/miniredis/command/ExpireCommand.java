package com.mohamed.halim.miniredis.command;

import com.mohamed.halim.miniredis.datastore.DataStore;
import com.mohamed.halim.miniredis.resp.RespEncoder;
import com.mohamed.halim.miniredis.utils.DataUtils;

import java.util.List;

public class ExpireCommand implements Command {

    @Override
    public byte[] execute(List<String> command) {
        if (command.size() < 3) {
            return RespEncoder.getInstance().encodeError(
                    "ERR",
                    "wrong number of arguments for 'expire' command"
            );
        }
        var encoder = RespEncoder.getInstance();
        var store = DataStore.getInstance();
        var ttl = DataUtils.parse(command.get(2));
        var timeoutSet = store.expire(command.get(1), ttl * 1000);
        return encoder.encodeInteger(timeoutSet ? 1 : 0);
    }


}
