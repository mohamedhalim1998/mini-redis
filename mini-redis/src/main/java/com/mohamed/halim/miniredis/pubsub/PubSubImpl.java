package com.mohamed.halim.miniredis.pubsub;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class PubSubImpl implements PubSub {
    public Map<String, List<Subscription>> subscriptions = new ConcurrentHashMap<>();

    @Override
    public int subscribe(String subscriberId, BiConsumer<String, String> listener, String... channels) {
        for(var channel : channels) {
            var channelSubs = subscriptions.getOrDefault(channel, new ArrayList<>());
            var alreadyFound = channelSubs.stream().anyMatch(subscription -> subscriberId.equals(subscription.getId()));
            if(alreadyFound) {
                continue;
            }
            channelSubs.add(new Subscription(subscriberId, listener));
            subscriptions.put(channel, channelSubs);
        }
        return countActiveSubscriptions(subscriberId);
    }

    private int countActiveSubscriptions(String subscriberId) {
        return (int) subscriptions.values().stream()
                .flatMap(Collection::stream)
                .filter(subscription -> subscriberId.equals(subscription.getId()))
                .count();
    }

    @Override
    public int unsubscribe(String subscriberId, String... channels) {
        for(var channel : channels) {
            var channelSubs = subscriptions.getOrDefault(channel, new ArrayList<>());
            var newSubs = channelSubs.stream()
                    .filter(subscription -> !subscriberId.equals(subscription.getId()))
                    .collect(Collectors.toCollection(ArrayList::new));
            subscriptions.put(channel, newSubs);
        }
        return countActiveSubscriptions(subscriberId);
    }

    @Override
    public int publish(String channel, String message) {
        var channelSubs = subscriptions.getOrDefault(channel, new ArrayList<>());
        channelSubs.forEach(subscription -> {
            subscription.getListener().accept(channel, message);
        });
        return channelSubs.size();
    }

    @Override
    public Set<String> activeChannels() {
        return subscriptions.keySet();
    }

    @Override
    public void removeSubscriber(String subscriberId) {
        for(var channel : subscriptions.keySet()) {
            var channelSubs = subscriptions.getOrDefault(channel, new ArrayList<>());
            var newSubs = channelSubs.stream()
                    .filter(subscription -> !subscriberId.equals(subscription.getId()))
                    .collect(Collectors.toCollection(ArrayList::new));
            if(newSubs.isEmpty()) {
                subscriptions.remove(channel);
            } else {
                subscriptions.put(channel, newSubs);
            }
        }
    }
}
