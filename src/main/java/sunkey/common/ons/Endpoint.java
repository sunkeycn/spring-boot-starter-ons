package sunkey.common.ons;

import lombok.*;

/**
 * @author Sunkey
 * @see com.aliyun.openservices.ons.api.PropertyKeyConst
 * @since 2019-01-09 下午2:34
 * <p>
 * Aliyun ONS Config Properties
 **/
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Endpoint {

    /**
     * 生产者/消费者ID
     */
    private String groupId;
    /**
     * 接入Key
     */
    private String accessKey;
    /**
     * 秘钥
     */
    private String secretKey;
    /**
     * 接入点地址
     */
    private String nameSrvAddr;

}
