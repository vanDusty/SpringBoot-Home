/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: User
 * Author:   zhangfan
 * Date:     2019-04-19 16:14
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.van.order.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author zhangfan
 * @create 2019-04-19
 * @since 1.0.0
 */

public class OrderDomain implements Serializable {
    private Integer id;
    private Integer orderNum;
    private LocalDateTime gmtCreate;

    public OrderDomain() {

    }

    public OrderDomain(Integer id, Integer orderNum, LocalDateTime gmtCreate) {
        this.id = id;
        this.orderNum = orderNum;
        this.gmtCreate = gmtCreate;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public LocalDateTime getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(LocalDateTime gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    @Override
    public String toString() {
        return "OrderDomain:{" +
                "id=" + id +
                ", orderNum='" + orderNum + '\'' +
                ", gmtCreate='" + gmtCreate + '\'' +
                '}';
    }
}
