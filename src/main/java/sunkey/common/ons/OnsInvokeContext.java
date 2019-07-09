package sunkey.common.ons;

import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import sunkey.common.invoke.InvokeContext;

/**
 * @author Sunkey
 * @since 2019-03-04 19:51
 **/

@Getter
@ToString
@AllArgsConstructor
public class OnsInvokeContext implements InvokeContext {

    private Message message;
    private ConsumeContext context;

}
