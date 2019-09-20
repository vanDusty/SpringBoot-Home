package cn.van.redis.lock.mapper;

import cn.van.redis.lock.entity.FamilyRewardRecordDO;

import java.util.Map;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: FamilyRewardRecordMapper
 *
 * @author: Van
 * Date:     2019-09-17 23:47
 * Description:
 * Version： V1.0
 */
public interface FamilyRewardRecordMapper {
    /**
     * 插入一条记录
     * @param record
     * @return
     */
    int insert(FamilyRewardRecordDO record);

    /**
     * 根据家庭id和奖励类型查询记录
     * @param map
     * @return
     */
    int selectCountByFamilyIdAndRewardType(Map map);


}