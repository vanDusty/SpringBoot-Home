package cn.van.shiro.mapper;

import cn.van.shiro.entity.SysRoleDO;

public interface SysRoleDao {
    int deleteByPrimaryKey(Long id);

    int insert(SysRoleDO record);

    int insertSelective(SysRoleDO record);

    SysRoleDO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysRoleDO record);

    int updateByPrimaryKey(SysRoleDO record);
}