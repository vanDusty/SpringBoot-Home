package cn.van.rocket.demo.web.controller;

import cn.van.rocket.demo.bean.MqMsgBean;
import cn.van.rocket.demo.config.AliRocketMqCfg;
import cn.van.rocket.demo.producer.ProducerWrapper;
import cn.van.rocket.demo.util.MqSerializationUtils;
import com.aliyun.openservices.ons.api.Message;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @公众号： 风尘博客
 * @Classname RocketMQController
 * @Description TODO
 * @Date 2020/3/6 3:58 下午
 * @Author by Van
 */
@RestController
public class RocketMqController {


    @Resource
    ProducerWrapper producerWrapper;


    @GetMapping("/sendMsg")
    public boolean sendMsg() {
        Message msg = new Message();
        msg.setTopic(AliRocketMqCfg.getTransferProcessTopic());
        msg.setTag(AliRocketMqCfg.getTransferProcessTopicTagMainOrderTransferStart());
        // Message Body 可以是任何二进制形式的数据，消息队列 RocketMQ 版不做任何干预，需要 Producer 与 Consumer 协商好一致的序列化和反序列化方式
        msg.setBody(MqSerializationUtils.serialize(new MqMsgBean().setBatchId(1L)));
        // 设置代表消息的业务关键属性，请尽可能全局唯一。
        // 以方便您在无法正常收到消息情况下，可通过控制台查询消息并补发。
        // 注意：不设置也不会影响消息正常收发
        msg.setKey("1");
        return producerWrapper.send(msg);
    }

    @GetMapping("/receive")
    public String receive() {

        return "success";
    }
}
