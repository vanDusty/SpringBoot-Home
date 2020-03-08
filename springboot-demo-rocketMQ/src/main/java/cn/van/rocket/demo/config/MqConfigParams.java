package cn.van.rocket.demo.config;

import org.springframework.beans.factory.annotation.Value;

/**
 * @公众号： 风尘博客
 * @Classname MqConfigParams
 * @Description MQ 配置参数
 * @Date 2020/3/5 2:44 下午
 * @Author by Van
 */

public class MqConfigParams {

//    @Value("${mqGroupId}")
//    private String mqGroupId;
//    @Value("${mqAccessKey}")
//    private String mqAccessKey;
//    @Value("${mqSecretKey}")
//    private String mqSecretKey;
//    @Value("${mqNameSrvAddr}")
//    private String mqNameSrvAddr;

    /**
     * 测试ons配置文件
     */
    public static final String TOPIC = "zyt_order_process";
    public static final String GROUP_ID = "zyt_order_process";
    public static final String ACCESS_KEY = "你申请下来的accesskey";
    public static final String SECRET_KEY = "你申请下来的secretkey";
    public static final String ONS_ADDR = "http://onsaddr-internet.aliyun.com/rocketmq/nsaddr4client-internet";

}
