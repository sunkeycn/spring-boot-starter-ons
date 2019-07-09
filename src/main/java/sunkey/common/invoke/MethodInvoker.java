package sunkey.common.invoke;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Sunkey
 * @since 2019-03-04 18:51
 **/
public interface MethodInvoker<T extends InvokeContext> {

    Method getMethod();

    default boolean isVoid() {
        return getReturnType() == Void.TYPE;
    }

    default Class<?> getReturnType() {
        return getMethod().getReturnType();
    }

    Object invoke(Object target, T context) throws InvocationTargetException;

}
