package sunkey.common.ons;

import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.SendResult;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * @author Sunkey
 * @since 2019-05-07 10:02
 **/
@RequiredArgsConstructor
public class SendableMessageBuilder extends MessageBuilder<SendableMessageBuilder> {

    public static SendableMessageBuilder newBuilder(Producer producer) {
        return new SendableMessageBuilder(producer);
    }

    @NonNull
    private final Producer producer;

    public SendResult send() {
        return producer.send(build());
    }

}
