package cn.van.shiro.mapper;

import cn.van.shiro.entity.SysUserRoleDO;

public interface SysUserRoleDao {
    int deleteByPrimaryKey(Long id);

    int insert(SysUserRoleDO record);

    int insertSelective(SysUserRoleDO record);

    SysUserRoleDO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysUserRoleDO record);

    int updateByPrimaryKey(SysUserRoleDO record);
}