package sunkey.common.invoke;

import lombok.Getter;
import lombok.ToString;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @author Sunkey
 * @since 2019-03-04 19:17
 **/

@Getter
@ToString
public class Argument {

    private final Method method;
    private final int index;
    private final Parameter parameter;
    private final String name;

    Argument(Method method, int index, String name) {
        this.method = method;
        this.index = index;
        this.parameter = method.getParameters()[index];
        this.name = name;
    }

    public <T extends Annotation> T getAnnotation(Class<T> aType) {
        return parameter.getAnnotation(aType);
    }

    public <T extends Annotation> T findAnnotation(Class<T> aType) {
        return AnnotatedElementUtils.findMergedAnnotation(parameter, aType);
    }

    public Class<?> getType() {
        return parameter.getType();
    }

    public boolean isInstance(Object target) {
        return getType().isInstance(target);
    }

    public boolean isType(Class<?> type) {
        return getType().isAssignableFrom(type);
    }

    public boolean isOneOfType(Class<?>... types) {
        for (Class<?> type : types) {
            if (isType(type)) return true;
        }
        return false;
    }

    public boolean isName(String name) {
        return this.name.equals(name);
    }

    public boolean isOneOfName(String... names) {
        for (String name : names) {
            if (isName(name)) return true;
        }
        return false;
    }

    public ArgumentCannotResolveException cannotResolve() {
        throw new ArgumentCannotResolveException(this);
    }

}
