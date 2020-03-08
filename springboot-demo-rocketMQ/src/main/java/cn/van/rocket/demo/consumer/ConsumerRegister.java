package cn.van.rocket.demo.consumer;

import com.aliyun.openservices.ons.api.Consumer;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * @公众号： 风尘博客
 * @Classname ConsumerRegister
 * @Description TODO
 * @Date 2020/3/6 12:19 下午
 * @Author by Van
 */
@Slf4j
@Component("consumerRegister")
public class ConsumerRegister implements InitializingBean {

    @Value("${mqGroupId}")
    private String mqGroupId;
    @Value("${mqAccessKey}")
    private String mqAccessKey;
    @Value("${mqSecretKey}")
    private String mqSecretKey;
    @Value("${mqNameSrvAddr}")
    private String mqNameSrvAddr;

    @Resource
    private ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() {
        log.info("afterPropertiesSet enter");
        Properties properties = new Properties();
        // 您在控制台创建的 Group ID
        properties.put(PropertyKeyConst.GROUP_ID, mqGroupId);
        // 鉴权用 AccessKey，在阿里云服务器管理控制台创建
        properties.put(PropertyKeyConst.AccessKey, mqAccessKey);
        // 鉴权用 SecretKey，在阿里云服务器管理控制台创建
        properties.put(PropertyKeyConst.SecretKey, mqSecretKey);
        // 设置 TCP 接入域名，进入控制台的实例管理页面，在页面上方选择实例后，在实例信息中的“获取接入点信息”区域查看
        properties.put(PropertyKeyConst.NAMESRV_ADDR, mqNameSrvAddr);
        //- 消费线程数
        properties.put(PropertyKeyConst.ConsumeThreadNums, 50);
        Consumer consumer = ONSFactory.createConsumer(properties);
        Map<String, IBizMessageListener> iBizMessageListenerMap = applicationContext.getBeansOfType(IBizMessageListener.class);
        log.info("iBizMessageListenerMap.keySet={}", iBizMessageListenerMap.keySet());
        Map<String, List<IBizMessageListener>> listenersPerTopic = iBizMessageListenerMap.values().stream().collect(Collectors.groupingBy(IBizMessageListener::getTopic));
        for (String topic : listenersPerTopic.keySet()) {
            String subExpression = null;
            consumer.subscribe(topic, subExpression, new TopicMessageListenerWrapper(topic, listenersPerTopic.get(topic)));
            log.info("subscribed topic={}", topic);
        }
        consumer.start();
        log.info("Consumer Started");
    }
}

