package com.mohamed.halim.miniredis.server;

import com.mohamed.halim.miniredis.command.CommandExecutor;
import com.mohamed.halim.miniredis.command.CommandExecutorImpl;
import com.mohamed.halim.miniredis.resp.RespParser;
import com.mohamed.halim.miniredis.resp.RespParserImpl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class EventLoop implements Runnable {

    private final Selector selector;
    private boolean running;
    private final RespParser parser;
    private final CommandExecutor commandExecutor;

    public static final int BUFFER_SIZE = 1024;

    public EventLoop(int port) throws IOException {
        this.running = true;
        this.parser = new RespParserImpl();
        this.commandExecutor = new CommandExecutorImpl();
        selector = Selector.open();
        ServerSocketChannel channel = ServerSocketChannel.open();
        channel.configureBlocking(false);
        channel.bind(new InetSocketAddress(port));
        channel.register(selector, SelectionKey.OP_ACCEPT);
    }

    public void stop() {
        this.running = false;
    }

    @Override
    public void run() {
        while (running) {
            try {
                selector.select(10);
                var keys = selector.selectedKeys().iterator();
                while (keys.hasNext()) {
                    var key = keys.next();
                    keys.remove();
                    handleKey(key);
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Something went wrong");
            }
        }
    }

    private void handleKey(SelectionKey key) throws IOException {
        if (!key.isValid()) {
            return;
        }
        if (key.isAcceptable()) {
            acceptConnection(key);
        }
        if (key.isReadable()) {
            readData(key);
        }
        if (key.isValid() && key.isWritable()) {
            writeData(key);
        }
    }

    private void writeData(SelectionKey key) throws IOException {
        SocketChannel client = (SocketChannel) key.channel();
        ClientContext ctx = (ClientContext) key.attachment();

        if (ctx == null) return;

        // Get existing buffer or create one if this is a new write operation
        ByteBuffer buffer = ByteBuffer.wrap(ctx.output().toByteArray());

        // Perform non-blocking write
        client.write(buffer);

        System.out.println(new String(buffer.array()));

        // Check if the buffer was completely written
        if (!buffer.hasRemaining()) {
            ctx.output().reset();
            key.interestOps(key.interestOps() & ~SelectionKey.OP_WRITE);
        }
    }

    // fixme:  for now that command can be read in a single read (size less than BUFFER_SIZE)
    private void readData(SelectionKey key) throws IOException {
        var client = (SocketChannel) key.channel();
        var ctx = (ClientContext) key.attachment();
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        int noOfBytes = client.read(buffer);
        if (noOfBytes <= -1) {
            System.out.println("Client disconnected: " + client.getRemoteAddress());
            client.close();
            return;
        }
        if (noOfBytes == 0) {
            return;
        }
        buffer.flip();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        ctx.setInput(new String(bytes, StandardCharsets.UTF_8));
        var command = parser.parseCommand(new ByteArrayInputStream(bytes));
        ctx.output().write(commandExecutor.execute(command));
        key.interestOps(SelectionKey.OP_WRITE | SelectionKey.OP_READ);
    }


    private void acceptConnection(SelectionKey key) throws IOException {
        var server = (ServerSocketChannel) key.channel();
        var client = server.accept();
        if (client != null) {
            client.configureBlocking(false);
            client.register(key.selector(), SelectionKey.OP_READ, new ClientContext());
            System.out.println("Open connection with: " + client.getRemoteAddress());
        }
    }


}
