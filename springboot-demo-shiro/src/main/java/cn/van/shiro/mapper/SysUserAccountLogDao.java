package cn.van.shiro.mapper;

import cn.van.shiro.entity.SysUserAccountLogDO;

public interface SysUserAccountLogDao {
    int deleteByPrimaryKey(Long id);

    int insert(SysUserAccountLogDO record);

    int insertSelective(SysUserAccountLogDO record);

    SysUserAccountLogDO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysUserAccountLogDO record);

    int updateByPrimaryKey(SysUserAccountLogDO record);
}