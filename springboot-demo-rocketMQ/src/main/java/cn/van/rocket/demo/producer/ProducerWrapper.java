package cn.van.rocket.demo.producer;

/**
 * @公众号： 风尘博客
 * @Classname CloudMQUtil
 * @Description TODO
 * @Date 2020/3/5 2:46 下午
 * @Author by Van
 */
import cn.van.rocket.demo.bean.LocalTransactionExecuterWrapper;
import com.aliyun.openservices.ons.api.*;
import com.aliyun.openservices.ons.api.transaction.LocalTransactionChecker;
import com.aliyun.openservices.ons.api.transaction.LocalTransactionExecuter;
import com.aliyun.openservices.ons.api.transaction.TransactionProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Properties;

@Component("producerWrapper")
@Slf4j
public class ProducerWrapper implements InitializingBean {

    @Value("${mqGroupId}")
    private String mqGroupId;
    @Value("${mqAccessKey}")
    private String mqAccessKey;
    @Value("${mqSecretKey}")
    private String mqSecretKey;
    @Value("${mqNameSrvAddr}")
    private String mqNameSrvAddr;

    private TransactionProducer transactionProducer;

    private Producer producer;

    @Resource
    private LocalTransactionChecker localTransactionChecker;

    /**
     * 获取消息的 Producer
     *
     * @return Producer
     */
    @Override
    public void afterPropertiesSet() {
        Properties properties = new Properties();

        // 您在控制台创建的 Group ID 注意：事务消息的 Group ID 不能与其他类型消息的 Group ID 共用
        properties.put(PropertyKeyConst.GROUP_ID, mqGroupId);
        // 阿里云身份验证，在阿里云服务器管理控制台创建
        properties.put(PropertyKeyConst.AccessKey, mqAccessKey);
        // 阿里云身份验证，在阿里云服务器管理控制台创建
        properties.put(PropertyKeyConst.SecretKey, mqSecretKey);
        // 设置 TCP 接入域名，进入控制台的实例管理页面的“获取接入点信息”区域查看
        properties.put(PropertyKeyConst.NAMESRV_ADDR, mqNameSrvAddr);
        // 初始化事务消息Producer时，需要注册一个本地事务状态的Checker
        TransactionProducer transactionProducer = ONSFactory.createTransactionProducer(properties, localTransactionChecker);
        transactionProducer.start();
        this.transactionProducer = transactionProducer;
        Producer producer = ONSFactory.createProducer(properties);
        // 在发送消息前，必须调用start方法来启动Producer，只需调用一次即可。
        producer.start();
        this.producer = producer;
        log.info("init done");
    }

    /**
     * 发送事务消息
     */
    public boolean send(Message message, LocalTransactionExecuter localTransactionExecuter) {
        log.info("send enter, message={}", message);
        try {
            // 在消息属性中添加第一次消息回查的最快时间，单位秒。例如，以下设置实际第一次回查时间为 120 秒 ~ 125 秒之间
            // 以下方式只确定事务消息的第一次回查的最快时间，实际回查时间向后浮动0~5秒；如第一次回查后事务仍未提交，后续每隔5秒回查一次。
            message.putUserProperties(PropertyKeyConst.CheckImmunityTimeInSeconds, "120");

            // DefaultMQPushConsumer.maxReconsumeTimes
            // https://help.aliyun.com/document_detail/43490.html
            // 如果消息重试 16 次后仍然失败，消息将不再投递。如果严格按照上述重试时间间隔计算，某条消息在一直消费失败的前提下，将会在接下来的 4 小时 46 分钟之内进行 16 次重试，超过这个时间范围消息将不再重试投递。
            // 最大重试次数大于 16 次，超过 16 次的重试时间间隔均为每次 2 小时。
            // 时间已经比较长，所以不再增加重试次数
            //message.putUserProperties(PropertyKeyConst.MaxReconsumeTimes, "28");

            SendResult sendResult = transactionProducer.send(message, new LocalTransactionExecuterWrapper(localTransactionExecuter), null);
            log.info("sendResult={}", sendResult);
            return sendResult != null;
        } catch (RuntimeException | Error throwable) {
            log.error("ex caught", throwable);
            return false;
        }
    }

    // ref: https://help.aliyun.com/document_detail/29549.html

    /**
     * 发送普通消息。包括：延迟消息。
     */
    public boolean send(Message message) {
        log.info("send enter, message={}", message);
        try {
            SendResult sendResult = producer.send(message);
            log.info("Send mq message success. message={}", message);
            return sendResult != null;
        } catch (RuntimeException | Error throwable) {
            // 消息发送失败，需要进行重试处理，可重新发送这条消息或持久化这条数据进行补偿处理
            log.error("Send mq message failed. message={}. msg={}", message, throwable);
            return false;
        }
    }



}
