package phase5;

import com.mohamed.halim.miniredis.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.assertj.core.api.Assertions.*;

@Tag("phase5")
@DisplayName("Pub/Sub")
class PubSubTest {

    // TODO: Replace with your implementation
    // private PubSub pubSub;

    // @BeforeEach
    // void setUp() {
    //     pubSub = new MiniRedisPubSub();
    // }

    @Test
    @DisplayName("should deliver message to subscriber")
    void shouldDeliverMessage() {
        // List<String> received = new CopyOnWriteArrayList<>();
        // pubSub.subscribe("sub1", (channel, msg) -> received.add(channel + ":" + msg), "news");
        //
        // int count = pubSub.publish("news", "hello");
        //
        // assertThat(count).isEqualTo(1);
        // assertThat(received).containsExactly("news:hello");
        fail("Implement PubSub and uncomment this test");
    }

    @Test
    @DisplayName("should deliver to multiple subscribers")
    void shouldDeliverToMultipleSubscribers() {
        // List<String> received1 = new CopyOnWriteArrayList<>();
        // List<String> received2 = new CopyOnWriteArrayList<>();
        // pubSub.subscribe("sub1", (ch, msg) -> received1.add(msg), "news");
        // pubSub.subscribe("sub2", (ch, msg) -> received2.add(msg), "news");
        //
        // int count = pubSub.publish("news", "update");
        //
        // assertThat(count).isEqualTo(2);
        // assertThat(received1).containsExactly("update");
        // assertThat(received2).containsExactly("update");
        fail("Implement PubSub and uncomment this test");
    }

    @Test
    @DisplayName("should return 0 when publishing to channel with no subscribers")
    void shouldReturnZeroForNoSubscribers() {
        // int count = pubSub.publish("empty", "hello");
        // assertThat(count).isEqualTo(0);
        fail("Implement PubSub and uncomment this test");
    }

    @Test
    @DisplayName("should not deliver after unsubscribe")
    void shouldNotDeliverAfterUnsubscribe() {
        // List<String> received = new CopyOnWriteArrayList<>();
        // pubSub.subscribe("sub1", (ch, msg) -> received.add(msg), "news");
        // pubSub.unsubscribe("sub1", "news");
        //
        // pubSub.publish("news", "hello");
        //
        // assertThat(received).isEmpty();
        fail("Implement PubSub and uncomment this test");
    }

    @Test
    @DisplayName("should subscribe to multiple channels")
    void shouldSubscribeToMultipleChannels() {
        // List<String> received = new CopyOnWriteArrayList<>();
        // pubSub.subscribe("sub1", (ch, msg) -> received.add(ch + ":" + msg), "ch1", "ch2");
        //
        // pubSub.publish("ch1", "msg1");
        // pubSub.publish("ch2", "msg2");
        //
        // assertThat(received).containsExactly("ch1:msg1", "ch2:msg2");
        fail("Implement PubSub and uncomment this test");
    }

    @Test
    @DisplayName("should report active channels")
    void shouldReportActiveChannels() {
        // pubSub.subscribe("sub1", (ch, msg) -> {}, "news", "sports");
        // assertThat(pubSub.activeChannels()).containsExactlyInAnyOrder("news", "sports");
        fail("Implement PubSub and uncomment this test");
    }

    @Test
    @DisplayName("should remove all subscriptions for disconnected subscriber")
    void shouldRemoveDisconnectedSubscriber() {
        // List<String> received = new CopyOnWriteArrayList<>();
        // pubSub.subscribe("sub1", (ch, msg) -> received.add(msg), "news");
        // pubSub.removeSubscriber("sub1");
        //
        // pubSub.publish("news", "hello");
        //
        // assertThat(received).isEmpty();
        // assertThat(pubSub.activeChannels()).isEmpty();
        fail("Implement PubSub and uncomment this test");
    }
}
