package cn.van.rocket.demo.config;

/**
 * @公众号： 风尘博客
 * @Classname AliRocketMqCfg
 * @Description 阿里云 RocketMQ 配置
 * @Date 2020/3/1 20:06 下午
 * @Author by Van
 */
public class AliRocketMqCfg {
    /**
     * 在控制台创建的 Topic
     */
    public static String getTransferProcessTopic() {
        return "zyt_transfer_process";
    }

    /**
     * Message Tag, 可理解为 Gmail 中的标签，对消息进行再归类，方便 Consumer 指定过滤条件在消息队列 RocketMQ 版服务器过滤
     */
    public static String getTransferProcessTopicTagMainOrderTransferStart() {
        return "rocket_mq_tag";
    }
}
