package cn.van.rocket.demo.consumer;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @公众号： 风尘博客
 * @Classname TopicMessageListenerWrapper
 * @Description  每个订阅者只能订阅某个tag。如果某个tag需要被多次消费，那需要新建消费者（Group）。
 * @Date 2020/3/6 12:29 下午
 * @Author by Van
 */
@Slf4j
public class TopicMessageListenerWrapper implements MessageListener {

    private final Map<String, IBizMessageListener> tagListenerMap = new ConcurrentHashMap<>(16, 0.5F);

    public TopicMessageListenerWrapper(String topic, List<IBizMessageListener> iBizMessageListenerList) {
        List<String> tagList = iBizMessageListenerList.stream().map(IBizMessageListener::getTag).collect(Collectors.toList());
        log.info("topic={}, tagList={}", topic, tagList);
        for (IBizMessageListener listener : iBizMessageListenerList) {
            String tag = listener.getTag();
            if (tag == null) {
                throw new RuntimeException("tag is null, listener.className=" + listener.getClass().getName());
            } else if ("*".equals(tag)) {
                throw new RuntimeException("tag is star, listener.className=" + listener.getClass().getName());
            }
            tagListenerMap.put(tag, listener);
        }
    }

    @Override
    public Action consume(Message message, ConsumeContext context) {
        log.info("d______________________");
        String tag = message.getTag();
        try {
            IBizMessageListener listener = tagListenerMap.get(tag);
            if (listener != null) {
                return listener.consume(message, context);
            } else {
                log.error("listener null, tag={}", tag);
                return Action.CommitMessage;
            }
        } catch (Throwable throwable) {
            // rocketmq不打印异常堆栈
            log.error("ex caught", throwable);
            return Action.ReconsumeLater;
        }
    }

}

