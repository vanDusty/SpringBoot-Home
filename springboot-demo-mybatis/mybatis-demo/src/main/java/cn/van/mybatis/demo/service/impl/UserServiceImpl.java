package cn.van.mybatis.demo.service.impl;

import cn.van.mybatis.demo.entity.UserDO;
import cn.van.mybatis.demo.mapper.UserMapper;
import cn.van.mybatis.demo.model.PageData;
import cn.van.mybatis.demo.model.PageParam;
import cn.van.mybatis.demo.service.UserService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: UserServiceImpl
 *
 * @author: Van
 * Date:     2019-07-26 15:07
 * Description: ${DESCRIPTION}
 * Version： V1.0
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Resource
    UserMapper userMapper;

    public UserDO selectById(Long id) {
        return userMapper.selectById(id);
    }

    public PageData selectForPage(PageParam pageParam) {
        // 使用PageHelper的api分页(指定页数和每页数量)
        PageHelper.startPage(pageParam.getPageNum(), pageParam.getPageSize());
        List<UserDO> list = userMapper.selectPage();
        // 包装Page对象
        PageData<UserDO> pageData = new PageData<UserDO>(list);
        return pageData;
    }
}
