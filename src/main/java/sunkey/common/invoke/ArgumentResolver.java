package sunkey.common.invoke;

public interface ArgumentResolver<T extends InvokeContext> {

    Object resolveArgument(T context, Argument argument);

    boolean canResolve(Argument argument);

}