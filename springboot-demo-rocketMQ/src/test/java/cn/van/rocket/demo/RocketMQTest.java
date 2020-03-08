package cn.van.rocket.demo;

import cn.van.rocket.demo.config.AliRocketMqCfg;
import cn.van.rocket.demo.bean.MqMsgBean;
import cn.van.rocket.demo.util.MqSerializationUtils;
import cn.van.rocket.demo.producer.ProducerWrapper;
import com.aliyun.openservices.ons.api.Message;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @公众号： 风尘博客
 * @Classname RocketMQTest
 * @Description TODO
 * @Date 2020/3/5 4:05 下午
 * @Author by Van
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class RocketMQTest {

    @Resource
    ProducerWrapper producerWrapper;

    @Test
    public void sendMsg() {
        Message msg = new Message();
        msg.setTopic(AliRocketMqCfg.getTransferProcessTopic());
        msg.setTag(AliRocketMqCfg.getTransferProcessTopicTagMainOrderTransferStart());
        // Message Body 可以是任何二进制形式的数据，消息队列 RocketMQ 版不做任何干预，需要 Producer 与 Consumer 协商好一致的序列化和反序列化方式
        msg.setBody(MqSerializationUtils.serialize(new MqMsgBean().setBatchId(1L)));
        // 设置代表消息的业务关键属性，请尽可能全局唯一。
        // 以方便您在无法正常收到消息情况下，可通过控制台查询消息并补发。
        // 注意：不设置也不会影响消息正常收发
        msg.setKey("1");
        boolean sendResult = producerWrapper.send(msg);
        System.out.println(sendResult);

    }

    @Test
    public void sendDelayMsg() {
        Message msg = new Message();
        msg.setTopic(AliRocketMqCfg.getTransferProcessTopic());
        msg.setTag(AliRocketMqCfg.getTransferProcessTopicTagMainOrderTransferStart());
        // Message Body 可以是任何二进制形式的数据，消息队列 RocketMQ 版不做任何干预，需要 Producer 与 Consumer 协商好一致的序列化和反序列化方式
        msg.setBody(MqSerializationUtils.serialize(new MqMsgBean().setBatchId(1L)));
        // 设置代表消息的业务关键属性，请尽可能全局唯一。
        // 以方便您在无法正常收到消息情况下，可通过控制台查询消息并补发。
        // 注意：不设置也不会影响消息正常收发
        msg.setKey("1");
        // 延时消息，单位毫秒（ms），在指定延迟时间（当前时间之后）进行投递，例如消息在 3 秒后投递
        long delayTime = System.currentTimeMillis() + 3000;

        // 设置消息需要被投递的时间
        msg.setStartDeliverTime(delayTime);
        boolean sendResult = producerWrapper.send(msg);
        System.out.println(sendResult);

    }

}
