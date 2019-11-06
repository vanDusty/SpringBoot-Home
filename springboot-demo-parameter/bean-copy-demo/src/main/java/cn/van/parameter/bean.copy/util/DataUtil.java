package cn.van.parameter.bean.copy.util;

import cn.van.parameter.bean.copy.entity.UserDO;
import cn.van.parameter.bean.copy.model.UserDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: DataUtil
 *
 * @author: Van
 * Date:     2019-11-02 18:32
 * Description: 造数据工具类
 * Version： V1.0
 */
public class DataUtil {

    /**
     * 模拟查询出一条数据
     * @return
     */
    public static UserDO createData() {
        return new UserDO(1, "Van", LocalDateTime.now(),new BigDecimal(100L));
    }

    /**
     * 模拟查询出多条数据
     * @param num 数量
     * @return
     */
    public static List<UserDO> createDataList(int num) {
        List<UserDO> userDOS = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            UserDO userDO = new UserDO(i+1, "Van", LocalDateTime.now(),new BigDecimal(100L));
            userDOS.add(userDO);
        }
        return userDOS;
    }
}
