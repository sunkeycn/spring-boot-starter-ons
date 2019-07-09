package sunkey.common.invoke;

import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import java.util.LinkedList;

/**
 * @author Sunkey
 * @since 2019-03-04 19:01
 **/
public class ArgumentResolvers<T extends InvokeContext>
        extends LinkedList<ArgumentResolver<T>>
        implements ArgumentResolver<T> {

    ArgumentResolvers() {
    }

    @Override
    public Object resolveArgument(T context, Argument argument) {
        for (ArgumentResolver<T> resolver : this) {
            if (resolver.canResolve(argument)) {
                return resolver.resolveArgument(context, argument);
            }
        }
        throw argument.cannotResolve();
    }

    @Override
    public boolean canResolve(Argument argument) {
        for (ArgumentResolver<T> resolver : this) {
            if (resolver.canResolve(argument)) {
                return true;
            }
        }
        return false;
    }

    public ArgumentResolver<T> findResolver(Argument argument) {
        for (ArgumentResolver<T> resolver : this) {
            if (resolver.canResolve(argument)) {
                return resolver;
            }
        }
        return null;
    }

    public void sort() {
        AnnotationAwareOrderComparator.sort(this);
    }

}
