/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: TestService
 * Author:   zhangfan
 * Date:     2019-03-19 15:05
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.van.service;

import cn.van.entity.ChildDO;
import cn.van.entity.ParentDO;
import javafx.scene.Parent;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author zhangfan
 * @create 2019-03-19
 * @since 1.0.0
 */
public interface TestService {
    ChildDO insertOne(ChildDO obj);

    ParentDO insert (ParentDO obj);
}