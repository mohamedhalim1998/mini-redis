package com.mohamed.halim.miniredis.persistence;

import com.mohamed.halim.miniredis.command.Command;
import com.mohamed.halim.miniredis.command.CommandExecutor;
import com.mohamed.halim.miniredis.command.CommandType;
import com.mohamed.halim.miniredis.command.SetCommand;
import com.mohamed.halim.miniredis.datastore.DataEntry;
import com.mohamed.halim.miniredis.datastore.DataStore;
import com.mohamed.halim.miniredis.utils.DataUtils;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class PersistenceImpl implements Persistence {
    private StringBuilder builder;
    private Path aof;


    public PersistenceImpl() {
        builder = new StringBuilder();
        aof = Path.of("aof.aof");
    }

    public PersistenceImpl(Path path) {
        builder = new StringBuilder();
        aof = path;
    }

    @Override
    public void appendToAof(List<String> command) throws IOException {
        var type = CommandType.valueOf(command.getFirst().toUpperCase(Locale.ROOT));
        switch (type) {
            case SET -> appendSetCommand(command);
            case EXPIRE -> appendExpireCommand(command);
            case PEXPIRE -> appendPexpireCommand(command);
            default -> {
                builder.append(String.join(" ", command));
                builder.append("\n");
            }
        }

    }

    private void appendExpireCommand(List<String> command) {
        builder.append("PEXPIREAT %s %d".formatted(command.get(1), DataUtils.unixMsFromTtl(command.get(2), TimeUnit.SECONDS)));

    }

    private void appendPexpireCommand(List<String> command) {
        builder.append("PEXPIREAT %s %d".formatted(command.get(1), DataUtils.unixMsFromTtl(command.get(2), TimeUnit.MILLISECONDS)));
    }

    private void appendSetCommand(List<String> command) {
        var params = DataUtils.buildParams(command);
        if(params.containsKey(SetCommand.Param.PX.name())) {
            builder.append(String.join(" ", command.subList(0, 3)));
            builder.append("\n");
            builder.append("PEXPIREAT %s %d".formatted(command.get(1), DataUtils.unixMsFromTtl(params.get(SetCommand.Param.PX.name()), TimeUnit.MILLISECONDS)));
            builder.append("\n");
        } else   if(params.containsKey(SetCommand.Param.EX.name())) {
            builder.append(String.join(" ", command.subList(0, 3)));
            builder.append("\n");
            builder.append("PEXPIREAT %s %d".formatted(command.get(1), DataUtils.unixMsFromTtl(params.get(SetCommand.Param.PX.name()), TimeUnit.SECONDS)));
            builder.append("\n");
        } else {
            builder.append(String.join(" ", command));
            builder.append("\n");
        }
    }

    @Override
    public int replayAof(CommandExecutor executor) throws IOException {
        if(!Files.exists(aof)) {
            return 0;
        }
        try (var lines = Files.lines(aof)) {
            return (int) lines.map(line -> {
                executor.execute(List.of(line.split(" ")));
                return line;
            }).count();
        }
    }

    @Override
    public void rewriteAof(DataStore store) throws IOException {
        var aofV2 = Path.of("aof_temp.txt");
        try (BufferedWriter writer = Files.newBufferedWriter(aofV2,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING)) {
            for (var e : store.entries()) {
                if(e.isExpired()) {
                    continue;
                }
                var commands = e.convertToCommand();
                for (String cmd : commands) {
                    writer.write(cmd);
                    writer.newLine();
                }
            }
            writer.flush();
        }
        if (Files.exists(aof)) {
            Files.move(aof, Path.of("aof_old.txt"), StandardCopyOption.REPLACE_EXISTING);
        }
        Files.move(aofV2, aof, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
    }

    @Override
    public void fsync() throws IOException {
        Files.writeString(aof, builder.toString(), StandardOpenOption.CREATE, StandardOpenOption.APPEND, StandardOpenOption.SYNC);
        builder = new StringBuilder();
    }

    @Override
    public void saveRdb(DataStore store, Path path) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(
                Files.newOutputStream(path,
                        StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING))) {

            for (var e : store.entries()) {
                if (e.isExpired()) {
                    continue;
                }
                out.writeObject(e);
            }
            out.flush();
        }
    }

    @Override
    public void loadRdb(DataStore store, Path path) throws IOException {
        try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(path))) {
            var entries = new ArrayList<DataEntry>();
            while (true) {
                try {
                    // Read and cast each object back to its target class
                    var entry = (DataEntry) in.readObject();
                    entries.add(entry);
                } catch (EOFException e) {
                    break;
                } catch (ClassNotFoundException e) {
                    throw new IOException(e);
                }
            }
            store.replaceEntries(entries);
        }
    }
}
