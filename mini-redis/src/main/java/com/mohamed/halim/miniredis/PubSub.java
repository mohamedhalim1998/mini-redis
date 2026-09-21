package com.mohamed.halim.miniredis;

import java.util.Set;
import java.util.function.BiConsumer;

/**
 * Manages publish/subscribe messaging between clients.
 *
 * <p>Pub/Sub is fire-and-forget: messages are delivered to all current
 * subscribers of a channel but are not persisted. If no subscribers
 * exist, the message is simply discarded.
 */
public interface PubSub {

    // --- Phase 5: Pub/Sub ---

    /**
     * Subscribe a client to one or more channels.
     *
     * @param subscriberId unique identifier for the subscribing connection
     * @param listener     callback invoked with (channel, message) when a message arrives
     * @param channels     the channels to subscribe to
     * @return the total number of channels this subscriber is now subscribed to
     */
    int subscribe(String subscriberId, BiConsumer<String, String> listener, String... channels);

    /**
     * Unsubscribe a client from one or more channels.
     * If no channels specified, unsubscribe from all.
     *
     * @param subscriberId the subscriber's identifier
     * @param channels     the channels to unsubscribe from (empty = all)
     * @return the total number of channels this subscriber remains subscribed to
     */
    int unsubscribe(String subscriberId, String... channels);

    /**
     * Publish a message to a channel.
     * Delivers the message to all subscribers of that channel.
     *
     * @param channel the target channel
     * @param message the message payload
     * @return the number of subscribers that received the message
     */
    int publish(String channel, String message);

    /**
     * Get the set of channels that currently have at least one subscriber.
     *
     * @return set of active channel names
     */
    Set<String> activeChannels();

    /**
     * Remove all subscriptions for a disconnected client.
     *
     * @param subscriberId the subscriber's identifier
     */
    void removeSubscriber(String subscriberId);
}
