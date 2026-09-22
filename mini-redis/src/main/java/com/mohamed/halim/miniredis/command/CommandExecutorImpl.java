package com.mohamed.halim.miniredis.command;

import com.mohamed.halim.miniredis.resp.RespEncoder;

import java.util.List;
import java.util.Map;

public class CommandExecutorImpl implements CommandExecutor {
    private final Map<CommandType, Command> commandMap;

    public CommandExecutorImpl() {
        this.commandMap = Map.ofEntries(
                Map.entry(CommandType.PING, new PingCommand()),
                Map.entry(CommandType.ECHO, new EchoCommand()),
                Map.entry(CommandType.SET, new SetCommand()),
                Map.entry(CommandType.GET, new GetCommand()),
                Map.entry(CommandType.INCR, new IncrCommand()),
                Map.entry(CommandType.DECR, new DecrCommand())
        );
    }

    @Override
    public byte[] execute(List<String> command) {
        if (command == null || command.isEmpty()) {
            return RespEncoder.getInstance().encodeError("ERR", "Not a valid command");
        }
        var type = getType(command.getFirst());
        if (type == null) {
            return RespEncoder.getInstance().encodeError("ERR", " unknown command '%s'".formatted(command.getFirst()));
        }
        try {
            return commandMap.get(type).execute(command);
        } catch (Exception e) {
            return RespEncoder.getInstance().encodeError("ERR", e.getMessage());
        }
    }

    private static CommandType getType(String command) {
        try {
            return CommandType.valueOf(command);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
