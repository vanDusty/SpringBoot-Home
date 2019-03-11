/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: MyProps
 * Author:   zhangfan
 * Date:     2019-03-11 10:35
 * Description: 接受自定义多线程配置的bean
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.van.task.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 〈一句话功能简述〉<br> 
 * 〈接受自定义多线程配置的bean〉
 *
 * @author zhangfan
 * @create 2019-03-11
 * @since 1.0.0
 */
//@Component
//@ConfigurationProperties(prefix = "myProps")
public class MyProps {

    private int corePoolSize ;
    private int maxPoolSize ;
    private int queueCapacity;

    public MyProps() {
    }


    public int getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public int getQueueCapacity() {
        return queueCapacity;
    }

    public void setQueueCapacity(int queueCapacity) {
        this.queueCapacity = queueCapacity;
    }
}