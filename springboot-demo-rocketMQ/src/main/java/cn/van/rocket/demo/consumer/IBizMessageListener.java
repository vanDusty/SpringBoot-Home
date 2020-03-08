package cn.van.rocket.demo.consumer;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;

/**
 * @公众号： 风尘博客
 * @Classname IBizMessageListener
 * @Description TODO
 * @Date 2020/3/6 12:26 下午
 * @Author by Van
 */
public interface IBizMessageListener {

    /**
     * 事务消息topic
     * @return
     */
    String getTopic();

    /**
     * 事务消息tag。
     * 如果没有tag，那就用*或null
     * @return
     */
    String getTag();

    // 来自 MessageListener，不继承，避免订阅的时候误用

    /**
     * 消费消息接口，由应用来实现<br>
     * 网络抖动等不稳定的情形可能会带来消息重复，对重复消息敏感的业务可对消息做幂等处理
     *
     * @param message 消息
     * @param context 消费上下文
     * @return 消费结果，如果应用抛出异常或者返回Null等价于返回Action.ReconsumeLater
     * @see <a href="https://help.aliyun.com/document_detail/44397.html">如何做到消费幂等</a>
     */
    Action consume(final Message message, final ConsumeContext context);

}