package cn.van.rocket.demo.bean;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @公众号： 风尘博客
 * @Classname MqMsgBean
 * @Description TODO
 * @Date 2020/3/5 4:10 下午
 * @Author by Van
 */
@Data
@Accessors(chain = true)
@ToString
public class MqMsgBean {

    /**
     * 主订单id
     */
    private Long batchId;
}
