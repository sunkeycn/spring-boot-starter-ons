package sunkey.common.ons;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.SendCallback;
import com.aliyun.openservices.ons.api.SendResult;
import lombok.AllArgsConstructor;

import java.nio.charset.StandardCharsets;

/**
 * @author Sunkey
 * @since 2019-03-05 10:34
 **/

@AllArgsConstructor
public class OnsProducer {

    private final Producer producer;

    public SendResult send(Message message) {
        return producer.send(message);
    }

    public void sendAsync(Message message, SendCallback sendCallback) {
        producer.sendAsync(message, sendCallback);
    }

    /**
     * 发送ONS消息
     *
     * @param topic topic of message
     * @param tag   tag of message
     * @param data  data of message
     * @return messageId
     */
    public String send(String topic, String tag, byte[] data) {
        return send(new Message(topic, tag, "", data)).getMessageId();
    }

    /**
     * 发送ONS消息
     * Data使用UTF-8加密
     *
     * @param topic topic of message
     * @param tag   tag of message
     * @param data  encode with UTF-8
     * @return messageId
     */
    public String send(String topic, String tag, String data) {
        return send(topic, tag, data.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 流式API发送消息
     *
     * @return
     */
    public SendableMessageBuilder message() {
        return SendableMessageBuilder.newBuilder(this.producer);
    }

    /**
     * 流式API发送消息, 提供topic和tag参数
     *
     * @return
     */
    public SendableMessageBuilder message(String topic, String tag) {
        return message().topic(topic).tag(tag);
    }

}
