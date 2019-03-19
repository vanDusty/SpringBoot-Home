/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: TestServiceImpl
 * Author:   zhangfan
 * Date:     2019-03-19 15:05
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.van.service.impl;

import cn.van.annotation.DataSource;
import cn.van.dao.ChildDao;
import cn.van.dao.ParentDao;
import cn.van.entity.ChildDO;
import cn.van.entity.ParentDO;
import cn.van.enums.DataSourceEnum;
import cn.van.service.TestService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author zhangfan
 * @create 2019-03-19
 * @since 1.0.0
 */
@Service
public class TestServiceImpl implements TestService {

    @Resource
    ParentDao parentDao;

    @Resource
    ChildDao childDao;


//    @DataSource(DataSourceEnum.DB1)
    @Override
    public ChildDO insertOne(ChildDO childDO) {
        childDao.insert(childDO);
        return null;
    }


    @DataSource(DataSourceEnum.DB2)
    @Override
    public ParentDO insert(ParentDO parentDO) {
        parentDao.insert(parentDO);
        return null;
    }
}