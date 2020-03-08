package cn.van.rocket.demo.listener;

import cn.van.rocket.demo.bean.MqMsgBean;
import cn.van.rocket.demo.config.AliRocketMqCfg;
import cn.van.rocket.demo.consumer.IBizMessageListener;
import cn.van.rocket.demo.util.MqSerializationUtils;
import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.shade.com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @公众号： 风尘博客
 * @Classname MainOrderTransferStartMessageListener
 * @Description TODO
 * @Date 2020/3/6 4:02 下午
 * @Author by Van
 */
@Service
@Slf4j
public class MainOrderTransferStartMessageListener implements IBizMessageListener {


    @Override
    public Action consume(Message paramMessage, ConsumeContext context) {
        log.info("message={}", paramMessage);
        MqMsgBean msgBean = MqSerializationUtils.deserialize(paramMessage.getBody(), MqMsgBean.class);
        log.info("msgBean={}", JSON.toJSONString(msgBean));
        long batchId = msgBean.getBatchId();
        log.info("batchId......{}", batchId);
        return true ? Action.CommitMessage : Action.ReconsumeLater;
    }

    @Override
    public String getTopic() {
        return AliRocketMqCfg.getTransferProcessTopic();
    }

    @Override
    public String getTag() {
        return AliRocketMqCfg.getTransferProcessTopicTagMainOrderTransferStart();
    }
}

