package sunkey.common.invoke;

import lombok.Getter;
import org.springframework.core.ParameterNameDiscoverer;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @author Sunkey
 * @since 2019-03-04 19:20
 **/

@Getter
public class MethodArguments {

    private Method method;
    private final Argument[] arguments;
    private ParameterNameDiscoverer discoverer;

    MethodArguments(Method method) {
        this(method, null);
    }

    MethodArguments(Method method, ParameterNameDiscoverer discoverer) {
        this.method = method;
        this.arguments = new Argument[method.getParameterCount()];
        this.discoverer = discoverer;
        resolveArguments();
    }

    public int length() {
        return arguments.length;
    }

    public Argument get(int i) {
        return arguments[i];
    }

    private void resolveArguments() {
        Parameter[] parameters = method.getParameters();
        String[] parameterNames = null;
        if (discoverer != null && parameters.length > 0) {
            if ("arg0".equals(parameters[0].getName())) {
                parameterNames = discoverer.getParameterNames(method);
            }
        }
        for (int i = 0; i < arguments.length; i++) {
            arguments[i] = new Argument(method, i, parameterNames == null ?
                    parameters[i].getName() : parameterNames[i]);
        }
    }

}
