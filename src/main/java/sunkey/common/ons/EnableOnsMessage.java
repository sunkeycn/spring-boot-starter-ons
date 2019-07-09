package sunkey.common.ons;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Sunkey
 * @since 2019-01-09 下午3:37
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(OnsAutoConfiguration.class)
public @interface EnableOnsMessage {

    String groupId() default "${ons.groupId}";

    String accessKey() default "${ons.accessKey}";

    String secretKey() default "${ons.secretKey}";

    String nameSrvAddr() default "${ons.nameSrvAddr}";

}
