package sunkey.common.invoke;

/**
 * @author Sunkey
 * @since 2019-03-04 19:07
 **/
public class ArgumentCannotResolveException extends RuntimeException {

    ArgumentCannotResolveException(Argument argument) {
        super("can't resolve parameter " + argument.getName() + " of method " + argument.getMethod());
    }

}
