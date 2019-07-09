### 使用介绍

```
@SpringBootApplication
@EnableOnsMessage
public class MyApplication {
    public static void main(String[] args){
        SpringApplication.run(MyApplication.class, args);
    }
}

@Component
public class MyMqMessageHandler {
    
    @MessageListener(topic="${ons.order.topic}", tags="*")
    public Action handleMyOrderMessage(String topic, String tag, String body, byte[] data, Message message, ConsumeContext context){
        // do something...
        return Action.CommitMessage;
    }
    
    @MessageListener(topic="MyTopic", tags="MyTag1||MyTag2||MyTag3")
    public void handleMyOrderMessage(Message message){
        // do something...
    }
    
    @MessageListener(topic="MyTopic")
    public void handleMyOrderMessage(String data) throws Exception {
        // do something...
    }
        
}

```