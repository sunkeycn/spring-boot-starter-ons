package sunkey.common.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.StringJoiner;

/**
 * @author Sunkey
 * @since 2019-04-16 14:05
 **/
public class Joiner {

    public static Joiner on(String joiner) {
        return new Joiner(joiner);
    }

    private final String joiner;

    private Joiner(String joiner) {
        this.joiner = joiner;
    }

    public String join(Iterator<String> iterator) {
        if (iterator == null) return "";
        StringJoiner sj = new StringJoiner(joiner);
        while (iterator.hasNext()) {
            sj.add(iterator.next());
        }
        return sj.toString();
    }

    public String join(Collection<String> list) {
        if (list == null || list.isEmpty()) return "";
        return join(list.iterator());
    }

    public String join(String... array) {
        if (array == null || array.length == 0) return "";
        return join(Arrays.asList(array));
    }

}
