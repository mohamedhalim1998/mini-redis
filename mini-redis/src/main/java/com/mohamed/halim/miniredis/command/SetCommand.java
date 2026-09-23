package com.mohamed.halim.miniredis.command;

import com.mohamed.halim.miniredis.datastore.DataStore;
import com.mohamed.halim.miniredis.resp.RespEncoder;
import com.mohamed.halim.miniredis.utils.DataUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SetCommand implements Command {
    public enum Param {
        EX,
        PX;
        private final boolean haveArgs;

        Param(boolean haveArgs) {
            this.haveArgs = haveArgs;
        }


        Param() {
            haveArgs = true;
        }

        public boolean haveArgs() {
            return haveArgs;
        }

        public static Param parseValue(String value) {
            try {
                return SetCommand.Param.valueOf(value);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("syntax error");
            }
        }
    }

    @Override
    public byte[] execute(List<String> command) {
        if (command.size() < 3) {
            return RespEncoder.getInstance().encodeError(
                    "ERR",
                    "wrong number of arguments for 'set' command"
            );
        }
        var encoder = RespEncoder.getInstance();
        var store = DataStore.getInstance();
        var params = buildParams(command);
        var ttl = extractTtl(params);
        if(ttl == -1) {
            store.set(command.get(1), command.get(2));
        } else {
            store.setWithTtl(command.get(1), command.get(2), ttl);
        }
        return encoder.encodeSimpleString("OK");
    }

    private long extractTtl(Map<String, String> args) {
        if(args.containsKey(Param.EX.name()) && args.containsKey(Param.PX.name())) {
            throw new RuntimeException("Syntax error");
        }
        if(args.containsKey(Param.EX.name())) {
            return DataUtils.parse(args.get(Param.EX.name())) * 1000;
        }
        if(args.containsKey(Param.PX.name())) {
            return DataUtils.parse(args.get(Param.PX.name()));
        }
        return -1;
    }

    private Map<String, String> buildParams(List<String> command) {
        // from 3 skipping command name, key and value
        var iterator = new ArrayList<>(command.subList(3, command.size())).iterator();
        var args = new HashMap<String, String>();
        while (iterator.hasNext()) {
            var param = SetCommand.Param.valueOf(iterator.next());
            if(param.haveArgs()) {
                args.put(param.name(), iterator.next());
            }
        }
        return args;
    }

}
