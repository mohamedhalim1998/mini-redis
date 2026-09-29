package com.mohamed.halim.miniredis.pubsub;

import java.util.function.BiConsumer;

public class Subscription {
    private String id;
    private BiConsumer<String, String> listener;
    public Subscription(String id, BiConsumer<String, String> listener) {
        this.id = id;
        this.listener = listener;
    }

    public String getId() {
        return id;
    }

    public BiConsumer<String, String> getListener() {
        return listener;
    }
}
