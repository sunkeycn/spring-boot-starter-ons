package sunkey.common.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Sunkey
 * @since 2019-04-16 14:01
 **/
public class Splitter {

    public static Splitter on(String splitter) {
        return new Splitter(splitter);
    }

    private final String splitter;

    private Splitter(String splitter) {
        if (StringUtils.isBlank(splitter))
            throw new IllegalArgumentException("splitter");
        this.splitter = splitter;
    }

    public List<String> list(String content) {
        if (StringUtils.isBlank(content))
            return new ArrayList<>();
        return StringUtils.split(content, splitter);
    }

    public String[] split(String content) {
        if (StringUtils.isBlank(content))
            return new String[0];
        List<String> list = list(content);
        return list.toArray(new String[list.size()]);
    }

    public Iterator<String> iterator(String content) {
        return list(content).iterator();
    }

}
