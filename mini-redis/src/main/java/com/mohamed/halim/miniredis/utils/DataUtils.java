package com.mohamed.halim.miniredis.utils;

import com.mohamed.halim.miniredis.command.SetCommand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class DataUtils {
    private DataUtils() {

    }

    public static Long parse(String value) {
        try {
            return Long.parseLong(value);
        } catch (Exception e) {
            throw new RuntimeException("(error) value is not an integer or out of range");
        }
    }

    public static Map<String, String> buildParams(List<String> command) {
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

    public static Long unixMsFromTtl(String ttl, TimeUnit timeUnit) {
        return System.currentTimeMillis() + timeUnit.toMillis(DataUtils.parse(ttl));
    }
}
