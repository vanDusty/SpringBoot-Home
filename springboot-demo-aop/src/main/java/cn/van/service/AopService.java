/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: AopServiceImpl
 * Author:   zhangfan
 * Date:     2019-02-27 14:40
 * Description: aop专用测试业务层
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.van.service;

/**
 * 〈一句话功能简述〉<br> 
 * 〈aop专用测试业务层〉
 *
 * @author zhangfan
 * @create 2019-02-27
 * @since 1.0.0
 */
public interface AopService {

    public String insertLog(Long id, String userName);

    public String throwException(String message);
}