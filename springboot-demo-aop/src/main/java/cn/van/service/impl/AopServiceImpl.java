/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: AopServiceImpl
 * Author:   zhangfan
 * Date:     2019-02-27 14:41
 * Description: Aop测试类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.van.service.impl;

import cn.van.service.AopService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 〈一句话功能简述〉<br> 
 * 〈Aop测试业务类〉
 *
 * @author zhangfan
 * @create 2019-02-27
 * @since 1.0.0
 */
@Service
public class AopServiceImpl implements AopService {


    @Override
    public String insertLog(Long id, String userName) {
        return "success";
    }

    @Override
    public String throwException(String message) {
        throw new RuntimeException(message);
    }
}