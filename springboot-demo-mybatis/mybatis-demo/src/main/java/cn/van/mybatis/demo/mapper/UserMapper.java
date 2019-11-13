package cn.van.mybatis.demo.mapper;

import cn.van.mybatis.demo.entity.UserDO;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: UserMapper
 *
 * @author: Van
 * Date:     2019-07-24 15:52
 * Description: ${DESCRIPTION}
 * Version： V1.0
 */
public interface UserMapper {

    /**
     * 查询单个用户
     * @param id
     * @return
     */
    UserDO selectById(Long id);

    /**
     * 分页查询用户
     * @return
     */
    List<UserDO> selectPage();
}
