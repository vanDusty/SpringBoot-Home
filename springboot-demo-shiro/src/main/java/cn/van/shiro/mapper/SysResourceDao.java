package cn.van.shiro.mapper;

import cn.van.shiro.entity.SysResourceDO;

public interface SysResourceDao {
    int deleteByPrimaryKey(Long id);

    int insert(SysResourceDO record);

    int insertSelective(SysResourceDO record);

    SysResourceDO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysResourceDO record);

    int updateByPrimaryKey(SysResourceDO record);
}