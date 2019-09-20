package cn.van.redis.lock.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: RedisLockApplication
 *
 * @author: Van
 * Date:     2019-09-17 23:47
 * Description: 家庭奖励记录
 * Version： V1.0
 */
@Data
public class FamilyRewardRecordDO{

    private Long id;

    private Long familyId;

    private Integer rewardType;

    private Integer state;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    public FamilyRewardRecordDO(){

    }
    public FamilyRewardRecordDO(Long familyId, Integer rewardType, Integer state, LocalDateTime createTime) {
        this.familyId = familyId;
        this.rewardType = rewardType;
        this.state = state;
        this.createTime = createTime;
    }
}