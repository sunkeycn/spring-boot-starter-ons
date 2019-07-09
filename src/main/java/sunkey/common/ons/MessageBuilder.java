package sunkey.common.ons;

import com.aliyun.openservices.ons.api.Message;
import lombok.Getter;
import sunkey.common.utils.JSON;

import java.nio.charset.StandardCharsets;
import java.util.Properties;

@Getter
public class MessageBuilder<T extends MessageBuilder<T>> {

    public static MessageBuilder newBuilder() {
        return new MessageBuilder();
    }

    private final Message message = new Message();

    public T topic(String topic) {
        message.setTopic(topic);
        return (T) this;
    }

    public T tag(String tag) {
        message.setTag(tag);
        return (T) this;
    }

    public T body(byte[] body) {
        message.setBody(body);
        return (T) this;
    }

    public T bodyUtf8(String body) {
        return body(body.getBytes(StandardCharsets.UTF_8));
    }

    public T jsonBody(Object body) {
        return bodyUtf8(JSON.toJSONString(body));
    }

    public T bornHost(String bornHost) {
        message.setBornHost(bornHost);
        return (T) this;
    }

    public T bornTimestamp(long bornTimestamp) {
        message.setBornTimestamp(bornTimestamp);
        return (T) this;
    }

    public T msgID(String msgID) {
        message.setMsgID(msgID);
        return (T) this;
    }

    public T shardingKey(String shardingKey) {
        message.setShardingKey(shardingKey);
        return (T) this;
    }

    public T userProperties(Properties userProperties) {
        message.setUserProperties(userProperties);
        return (T) this;
    }

    public T startDeliverTime(long startDeliverTime) {
        message.setStartDeliverTime(startDeliverTime);
        return (T) this;
    }

    public T key(String key) {
        message.setKey(key);
        return (T) this;
    }

    public T reconsumeTimes(int reconsumeTimes) {
        message.setReconsumeTimes(reconsumeTimes);
        return (T) this;
    }

    public Message build() {
        return message;
    }

}