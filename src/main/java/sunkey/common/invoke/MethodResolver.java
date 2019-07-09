package sunkey.common.invoke;

import org.springframework.core.DefaultParameterNameDiscoverer;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Sunkey
 * @since 2019-03-04 19:10
 **/
public class MethodResolver<T extends InvokeContext> extends ArgumentResolvers<T> {

    private final DefaultParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();
    private final Map<Method, MethodInvoker<T>> invokerCache = new ConcurrentHashMap<>();

    public MethodInvoker<T> getMethodInvoker(Method method) {
        return invokerCache.computeIfAbsent(method, this::create0);
    }

    private MethodInvoker<T> create0(Method method) {
        MethodArguments arguments = new MethodArguments(method, discoverer);
        ArgumentResolver<T>[] argResolvers = new ArgumentResolver[arguments.length()];
        for (int i = 0; i < argResolvers.length; i++) {
            Argument arg = arguments.get(i);
            argResolvers[i] = findResolver(arg);
            if (argResolvers[i] == null) {
                throw arg.cannotResolve();
            }
        }
        return new DefaultMethodInvoker<>(arguments, argResolvers);
    }

}
