package sunkey.common.invoke;

import lombok.Getter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Sunkey
 * @since 2019-03-04 19:06
 **/

@Getter
public class DefaultMethodInvoker<T extends InvokeContext> implements MethodInvoker<T> {

    private final MethodArguments arguments;
    private final ArgumentResolver<T>[] resolvers;

    DefaultMethodInvoker(MethodArguments arguments, ArgumentResolver<T>[] resolvers) {
        this.arguments = arguments;
        this.resolvers = resolvers;
        if (!arguments.getMethod().isAccessible()) {
            arguments.getMethod().setAccessible(true);
        }
    }

    @Override
    public Method getMethod() {
        return arguments.getMethod();
    }

    @Override
    public Object invoke(Object target, T context) throws InvocationTargetException {
        Object[] parameters = new Object[arguments.length()];
        for (int i = 0; i < parameters.length; i++) {
            parameters[i] = resolvers[i].resolveArgument(context, arguments.get(i));
        }
        try {
            return arguments.getMethod().invoke(target, parameters);
        } catch (IllegalAccessException e) {
            // ignore, never here.
            return null;
        }
    }

}
