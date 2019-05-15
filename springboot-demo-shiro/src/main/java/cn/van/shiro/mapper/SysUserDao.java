package cn.van.shiro.mapper;

import cn.van.shiro.entity.SysUserDO;

public interface SysUserDao {
    int deleteByPrimaryKey(Long id);

    int insert(SysUserDO record);

    int insertSelective(SysUserDO record);

    SysUserDO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysUserDO record);

    int updateByPrimaryKey(SysUserDO record);
}