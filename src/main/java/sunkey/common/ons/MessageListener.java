package sunkey.common.ons;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Sunkey
 * @since 2019-01-09 下午3:15
 **/

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MessageListener {

    /**
     * topic of message
     *
     * @return
     * @see com.aliyun.openservices.ons.api.Consumer#subscribe
     */
    String topic();

    /**
     * tags(subExpression) of message
     *
     * @return
     * @see com.aliyun.openservices.ons.api.Consumer#subscribe
     */
    String tags() default "*";

}
