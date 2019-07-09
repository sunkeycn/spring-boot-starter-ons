package sunkey.common.ons;

import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import org.springframework.core.annotation.Order;
import sunkey.common.invoke.Argument;
import sunkey.common.invoke.ArgumentResolver;

import java.nio.charset.StandardCharsets;

/**
 * @author Sunkey
 * @since 2019-03-05 11:38
 **/

@Order
public class OnsDefaultArgumentResolver implements ArgumentResolver<OnsInvokeContext> {
    @Override
    public Object resolveArgument(OnsInvokeContext context, Argument arg) {
        if (arg.isType(Message.class)) {
            return context.getMessage();
        }
        if (arg.isType(ConsumeContext.class)) {
            return context.getContext();
        }
        if (arg.isType(byte[].class)) {
            return context.getMessage().getBody();
        }
        if (arg.isType(String.class)) {
            switch (arg.getName()) {
                case "topic":
                    return context.getMessage().getTopic();
                case "tag":
                    return context.getMessage().getTag();
                case "data":
                case "body":
                    return new String(context.getMessage().getBody(), StandardCharsets.UTF_8);
                case "msgId":
                    return context.getMessage().getMsgID();
                case "key":
                    return context.getMessage().getKey();
            }
        }
        throw arg.cannotResolve();
    }

    @Override
    public boolean canResolve(Argument arg) {
        if (arg.isOneOfType(Message.class, ConsumeContext.class, byte[].class)) {
            return true;
        }
        if (arg.isType(String.class) && arg.isOneOfName("topic", "tag", "data", "body", "msgId", "key")) {
            return true;
        }
        return false;
    }
}
