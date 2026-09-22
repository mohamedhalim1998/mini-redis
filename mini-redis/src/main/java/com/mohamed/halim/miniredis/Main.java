package com.mohamed.halim.miniredis;

import com.mohamed.halim.miniredis.server.EventLoop;

import java.io.IOException;

public class Main {
    static void main() throws IOException {
        var server = new EventLoop(8080);
        server.run();
    }
}
