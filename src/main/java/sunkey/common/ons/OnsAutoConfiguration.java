package sunkey.common.ons;

import com.aliyun.openservices.ons.api.Consumer;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import sunkey.common.utils.StringUtils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Properties;

/**
 * @author Sunkey
 * @since 2019-03-04 18:29
 **/

@Slf4j
@Configuration
public class OnsAutoConfiguration implements
        ImportAware,
        ApplicationRunner,
        BeanPostProcessor,
        EnvironmentAware {

    @Setter
    private Environment environment;
    private Endpoint endpoint;
    private final TopicMapping mappings = new TopicMapping();

    private Consumer consumer = null;
    private Producer producer = null;

    @Override
    public void setImportMetadata(AnnotationMetadata metadata) {
        AnnotationAttributes attrs = AnnotationAttributes.fromMap(
                metadata.getAnnotationAttributes(EnableOnsMessage.class.getName()));
        String groupId = environment.resolvePlaceholders(
                attrs.getString("groupId"));
        String accessKey = environment.resolvePlaceholders(
                attrs.getString("accessKey"));
        String secretKey = environment.resolvePlaceholders(
                attrs.getString("secretKey"));
        String nameSrvAddr = environment.resolvePlaceholders(
                attrs.getString("nameSrvAddr"));
        if (!StringUtils.nonBlank(groupId, accessKey, secretKey, nameSrvAddr)) {
            throw new IllegalStateException(
                    String.format("MISSING PARAM: groupId '%s',accessKey '%s'," +
                                    "secretKey '%s'，nameSrvAddr '%s'",
                            groupId, accessKey, secretKey, nameSrvAddr));
        }
        this.endpoint = new Endpoint(groupId, accessKey, secretKey, nameSrvAddr);
        registerShutdownHook();
    }

    @Bean
    public OnsProducer onsProducer() {
        producer = ONSFactory.createProducer(buildConfig(endpoint));
        producer.start();
        log.info("[ONS] MessageProducer started with config : {}", endpoint);
        return new OnsProducer(producer);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (!mappings.isEmpty()) {
            // ons message handler exists. create consumer and start
            buildConsumer();
        }
    }

    private void buildConsumer() {
        consumer = ONSFactory.createConsumer(buildConfig(endpoint));
        for (Map.Entry<String, TopicMapping.Topic> entry : mappings.entrySet()) {
            String topic = entry.getKey();
            String tags = entry.getValue().getTags();
            com.aliyun.openservices.ons.api.MessageListener listener =
                    entry.getValue().getListener();
            consumer.subscribe(topic, tags, listener);
            log.info("Mapping MessageListener[{}.{}]=>{}",
                    topic, tags, listener);
        }
        consumer.start();
        log.info("[ONS] MessageConsumer started with config : {}", endpoint);
    }

    private void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (producer != null && producer.isStarted()) {
                log.info("ONS Producer has shutdown.");
                producer.shutdown();
            }
            if (consumer != null && consumer.isStarted()) {
                log.info("ONS Consumer has shutdown.");
                consumer.shutdown();
            }
        }));
    }

    private void findAndRegisterListener(Object bean, Method method) {
        MessageListener evt =
                AnnotatedElementUtils.findMergedAnnotation(method,
                        MessageListener.class);
        if (evt != null) {
            MessageListenerMeta meta = MessageListenerMeta.from(evt, environment);
            DynamicMessageListener listener = new DynamicMessageListener(bean, method);
            mappings.registerMessageListener(meta, listener);
        }
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName)
            throws BeansException {
        Class<?> type = AopUtils.getTargetClass(bean);
        for (Method method : type.getDeclaredMethods()) {
            findAndRegisterListener(bean, method);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName)
            throws BeansException {
        return bean;
    }

    private static Properties buildConfig(Endpoint endpoint) {
        Properties properties = new Properties();
        properties.setProperty(PropertyKeyConst.GROUP_ID, endpoint.getGroupId());
        properties.setProperty(PropertyKeyConst.AccessKey, endpoint.getAccessKey());
        properties.setProperty(PropertyKeyConst.SecretKey, endpoint.getSecretKey());
        properties.setProperty(PropertyKeyConst.NAMESRV_ADDR, endpoint.getNameSrvAddr());
        return properties;
    }

    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    class Subscriber {
        private final String topic;
        private final String tags;
    }

}
