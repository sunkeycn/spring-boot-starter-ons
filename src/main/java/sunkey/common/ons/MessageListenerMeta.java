package sunkey.common.ons;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.core.env.PropertyResolver;

/**
 * @author Sunkey
 * @since 2019-04-16 11:58
 **/
@Getter
@Setter
@ToString
public class MessageListenerMeta {

    private String topic;
    private String tags;

    public static MessageListenerMeta from(MessageListener ml,
                                           PropertyResolver resolver) {
        MessageListenerMeta meta = new MessageListenerMeta();
        meta.setTopic(resolver.resolvePlaceholders(ml.topic()));
        meta.setTags(resolver.resolvePlaceholders(ml.tags()));
        return meta;
    }

}
