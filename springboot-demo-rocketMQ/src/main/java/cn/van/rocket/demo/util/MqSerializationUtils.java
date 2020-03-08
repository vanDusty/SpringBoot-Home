package cn.van.rocket.demo.util;

import com.aliyun.openservices.shade.com.alibaba.fastjson.JSON;

import java.nio.charset.StandardCharsets;

/**
 * @公众号： 风尘博客
 * @Classname MqSerializationUtils
 * @Description Rocket MQ 序列化和反序列化工具类
 * @Date 2020/3/5 11:44 下午
 * @Author by Van
 */
public class MqSerializationUtils {

    /**
     * 序列化。格式：json字符串、UTF8编码
     */
    public static byte[] serialize(Object object) {
        if (object == null) {
            throw new NullPointerException("object null");
        }
        return JSON.toJSONString(object).getBytes(StandardCharsets.UTF_8);
    }

    /**
     * 反序列化为Java对象
     *
     * @param bytes 收到的消息body
     * @param clazz 消息对应的类类型
     * @return 反序列化出来的Java对象
     */
    public static  <T> T deserialize(byte[] bytes, Class<T> clazz) {
        String jsonStr = new String(bytes, StandardCharsets.UTF_8);
        return JSON.parseObject(jsonStr, clazz);
    }

}
