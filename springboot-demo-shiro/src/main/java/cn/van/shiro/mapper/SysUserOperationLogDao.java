package cn.van.shiro.mapper;

import cn.van.shiro.entity.SysUserOperationLogDO;

public interface SysUserOperationLogDao {
    int deleteByPrimaryKey(Long id);

    int insert(SysUserOperationLogDO record);

    int insertSelective(SysUserOperationLogDO record);

    SysUserOperationLogDO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysUserOperationLogDO record);

    int updateByPrimaryKey(SysUserOperationLogDO record);
}