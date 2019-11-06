package cn.van.parameter.bean.copy.demo;

import cn.van.parameter.bean.copy.entity.UserDO;
import cn.van.parameter.bean.copy.model.UserDTO;
import cn.van.parameter.bean.copy.util.DataUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: BeanUtilsDemo
 *
 * @author: Van
 * Date:     2019-11-02 18:30
 * Description: ${DESCRIPTION}
 * Version： V1.0
 */
@Slf4j
public class BeanUtilsDemo {

    public static void main(String[] args) {
        UserDO userDO = DataUtil.createData();
        log.info("拷贝前,userDO:{}", userDO);
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(userDO,userDTO);
        log.info("拷贝后,userDO:{}", userDO);
    }
}
