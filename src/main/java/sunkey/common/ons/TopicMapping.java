package sunkey.common.ons;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import lombok.Getter;
import lombok.ToString;
import sunkey.common.utils.Joiner;
import sunkey.common.utils.Splitter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Sunkey
 * @since 2019-04-16 11:53
 **/
@ToString
public class TopicMapping {

    private final Map<String, Topic> mappings = new HashMap<>();

    public boolean isEmpty() {
        return mappings.isEmpty();
    }

    public Set<Map.Entry<String, Topic>> entrySet() {
        return mappings.entrySet();
    }

    public void registerMessageListener(MessageListenerMeta meta, DynamicMessageListener listener) {
        Topic topic = mappings.computeIfAbsent(meta.getTopic(), Topic::new);
        topic.registerTags(meta, listener);
    }

    @ToString
    public static class Topic {

        private static final Joiner joiner = Joiner.on("||");
        private static final Splitter splitter = Splitter.on("||");

        @Getter
        private final String topic;
        private DynamicMessageListener matchAll = null;
        private final Map<String, DynamicMessageListener> listenerMap = new HashMap<>();

        private final MappingHandler listener = new MappingHandler();

        public Topic(String topic) {
            this.topic = topic;
        }

        public MessageListener getListener() {
            if (matchAll != null) {
                return matchAll;
            }
            if (listenerMap.size() == 1) {
                return listenerMap.values().iterator().next();
            }
            return listener;
        }

        public String getTags() {
            if (matchAll != null) return "*";
            if (listenerMap.size() == 1) {
                return listenerMap.keySet().iterator().next();
            }
            if (listenerMap.size() > 1) {
                return joiner.join(listenerMap.keySet());
            }
            return null;
        }

        public void registerTags(MessageListenerMeta meta, DynamicMessageListener listener) {
            if (matchAll != null) {
                throw new IllegalStateException("contains *");
            }
            if ("*".equals(meta.getTags())) {
                this.matchAll = listener;
            } else {
                List<String> tags = splitter.list(meta.getTags());
                for (String tag : tags) {
                    DynamicMessageListener origin = listenerMap.put(tag, listener);
                    if (origin != null) {
                        throw new IllegalStateException(
                                String.format("Duplicate MessageListener[%s.%s]", topic, tag));
                    }
                }
            }
        }

        class MappingHandler implements MessageListener {

            @Override
            public Action consume(Message message, ConsumeContext context) {
                DynamicMessageListener handler = listenerMap.get(message.getTag());
                if (handler == null) {
                    throw new IllegalStateException(
                            String.format("message [%s:%s] not mapping to handler!",
                                    message.getTopic(), message.getTag()));
                }
                return handler.consume(message, context);
            }

            @Override
            public String toString() {
                return listenerMap.toString();
            }
        }


    }


}
