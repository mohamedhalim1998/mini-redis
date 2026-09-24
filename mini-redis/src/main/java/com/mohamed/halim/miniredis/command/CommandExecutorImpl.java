package com.mohamed.halim.miniredis.command;

import com.mohamed.halim.miniredis.resp.RespEncoder;

import java.util.List;
import java.util.Locale;
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
                Map.entry(CommandType.DECR, new DecrCommand()),
                Map.entry(CommandType.EXPIRE, new ExpireCommand()),
                Map.entry(CommandType.PEXPIRE, new PexpireCommand()),
                Map.entry(CommandType.TTL, new TllCommand()),
                Map.entry(CommandType.PTTL, new PtllCommand()),
                Map.entry(CommandType.PERSIST, new PresistCommand()),
                Map.entry(CommandType.LPUSH, new LpushCommand()),
                Map.entry(CommandType.RPUSH, new RpushCommand()),
                Map.entry(CommandType.LPOP, new LpopCommand()),
                Map.entry(CommandType.RPOP, new RpopCommand()),
                Map.entry(CommandType.LLEN, new LlenCommand()),
                Map.entry(CommandType.LRANGE, new LrangeCommand()),
                Map.entry(CommandType.SADD, new SaddCommand()),
                Map.entry(CommandType.SREM, new SremCommand()),
                Map.entry(CommandType.SISMEMBER, new SisMemberCommand()),
                Map.entry(CommandType.SMEMBERS, new SmembersCommand()),
                Map.entry(CommandType.SCARD, new ScardCommand()),
                Map.entry(CommandType.HSET, new HsetCommand()),
                Map.entry(CommandType.HGET, new HgetCommand()),
                Map.entry(CommandType.HDEL, new HdelCommand()),
                Map.entry(CommandType.HGETALL, new HgetAllCommand()),
                Map.entry(CommandType.HEXISTS, new HexistsCommand()),
                Map.entry(CommandType.HLEN, new HlenCommand()),
                Map.entry(CommandType.TYPE, new TypeCommand()),
                Map.entry(CommandType.KEYS, new KeysCommand())
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
            return CommandType.valueOf(command.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
