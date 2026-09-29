package com.mohamed.halim.miniredis.command;

import com.mohamed.halim.miniredis.persistence.Persistence;
import com.mohamed.halim.miniredis.resp.RespEncoder;
import com.mohamed.halim.miniredis.server.ClientContext;

import java.io.IOException;
import java.util.*;

public class CommandExecutorImpl implements CommandExecutor {
    private final Map<CommandType, Command> commandMap;
    private final Persistence persistence;
    private final ClientContext defaultSession;

    public CommandExecutorImpl() {
        this.persistence = Persistence.getInstance();
        this.defaultSession = new ClientContext(UUID.randomUUID().toString());
        this.commandMap = Map.ofEntries(
                Map.entry(CommandType.PING, new PingCommand()),
                Map.entry(CommandType.ECHO, new EchoCommand()),
                Map.entry(CommandType.SET, new SetCommand()),
                Map.entry(CommandType.GET, new GetCommand()),
                Map.entry(CommandType.DEL, new DelCommand()),
                Map.entry(CommandType.INCR, new IncrCommand()),
                Map.entry(CommandType.DECR, new DecrCommand()),
                Map.entry(CommandType.EXPIRE, new ExpireCommand()),
                Map.entry(CommandType.PEXPIREAT, new PexpireAtCommand()),
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
                Map.entry(CommandType.KEYS, new KeysCommand()),
                Map.entry(CommandType.SUBSCRIBE, new SubscribeCommand()),
                Map.entry(CommandType.UNSUBSCRIBE, new UnsubscribeCommand()),
                Map.entry(CommandType.PUBLISH, new PublishCommand()),
                Map.entry(CommandType.MULTI, new MultiCommand())
        );
    }

    @Override
    public byte[] execute(List<String> command) {
        return execute(command, defaultSession);
    }

    @Override
    public byte[] execute(List<String> command, ClientContext context) {
        if (command == null || command.isEmpty()) {
            return RespEncoder.getInstance().encodeError("ERR", "Not a valid command");
        }
        var type = getType(command.getFirst());
        if (type == null) {
            return RespEncoder.getInstance().encodeError("ERR", " unknown command '%s'".formatted(command.getFirst()));
        }
        var mode = context.getMode();
        if (mode.equals(ClientContext.ClientMode.PUB_SUB) && type.isPubSub()) {
            return RespEncoder.getInstance().encodeError("ERR", "Client in pub/sub mode only pub/sub commands are allowed");
        }
        if (mode.equals(ClientContext.ClientMode.TRANSACTION)) {
            return handleTransaction(type, command, context);
        }

        try {
            var result = commandMap.get(type).execute(command, context);
            appendCommand(type, command);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return RespEncoder.getInstance().encodeError("ERR", e.getMessage());
        }
    }

    private byte[] handleTransaction(CommandType type, List<String> command, ClientContext context) {
        if (CommandType.EXEC.equals(type)) {
            context.setMode(ClientContext.ClientMode.DEFAULT);
            return RespEncoder.getInstance().encodeArray(
                    context.getQueuedCommands()
                            .stream()
                            .map(c -> this.execute(c, context))
                            .map(String::new)
                            .toList()
            );
        } else if (CommandType.DISCARD.equals(type)) {
            context.getQueuedCommands().clear();
            context.setMode(ClientContext.ClientMode.DEFAULT);
            return RespEncoder.getInstance().encodeSimpleString("OK");
        } else {
            context.addCommand(command);
            return RespEncoder.getInstance().encodeSimpleString("QUEUED");
        }
    }

    private void appendCommand(CommandType type, List<String> command) throws IOException {
        if (type.isModifyingCommand()) {
            persistence.appendToAof(command);
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
