package sunkey.common.ons;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import sunkey.common.invoke.MethodInvoker;
import sunkey.common.invoke.MethodResolver;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Slf4j
@Getter
@Setter
@ToString
public class DynamicMessageListener implements com.aliyun.openservices.ons.api.MessageListener {

    private static final MethodResolver<OnsInvokeContext> resolver;

    static {
        resolver = new MethodResolver<>();
        resolver.add(new OnsDefaultArgumentResolver());
    }

    private final Object target;
    private final MethodInvoker<OnsInvokeContext> invoker;

    DynamicMessageListener(Object target, Method method) {
        this.target = target;
        this.invoker = resolver.getMethodInvoker(method);
    }

    @Override
    public String toString() {
        return invoker.getMethod().getDeclaringClass().getSimpleName()
                + "." + invoker.getMethod().getName() + "()";
    }

    @Override
    public Action consume(Message message, ConsumeContext context) {
        Object result;
        try {
            result = invoker.invoke(target, new OnsInvokeContext(message, context));
        } catch (InvocationTargetException e) {
            log.error("[ONS] call {} error : {}", invoker.getMethod(),
                    e.getTargetException().getMessage(),
                    e.getTargetException());
            return Action.ReconsumeLater;
        }
        if (result != null && result instanceof Action) {
            return (Action) result;
        } else {
            return Action.CommitMessage;
        }
    }
}