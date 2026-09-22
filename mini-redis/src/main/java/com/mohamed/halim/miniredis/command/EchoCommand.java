package com.mohamed.halim.miniredis.command;

import com.mohamed.halim.miniredis.resp.RespEncoder;

import java.util.List;

public class EchoCommand implements Command {


    @Override
    public byte[] execute(List<String> command) {
        if(command.size() != 2) {
            return RespEncoder.getInstance().encodeError(
                    "ERR",
                    "wrong number of arguments for 'echo' command"
            );
        }
        return RespEncoder.getInstance().encodeBulkString(command.get(1));
    }
}
