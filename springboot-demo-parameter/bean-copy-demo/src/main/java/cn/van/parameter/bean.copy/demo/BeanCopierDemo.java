package cn.van.parameter.bean.copy.demo;

import cn.van.parameter.bean.copy.entity.UserDO;
import cn.van.parameter.bean.copy.model.UserDTO;
import cn.van.parameter.bean.copy.model.UserDomain;
import cn.van.parameter.bean.copy.model.UserEntity;
import cn.van.parameter.bean.copy.util.DataUtil;
import cn.van.parameter.bean.copy.util.UserConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.beans.BeanCopier;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: BeanCopierDemo
 *
 * @author: Van
 * Date:     2019-11-02 15:15
 * Description: ${DESCRIPTION}
 * Version： V1.0
 */
@Slf4j
public class BeanCopierDemo {
    /**
     * 使用缓存提高效率
     */
    private static final ConcurrentHashMap<String, BeanCopier> BEAN_COPIERS = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        // 属性名称、类型都相同
        normalCopy();
        // 属性名称相同、类型不同
        sameNameDifferentType();
        // 类型不同,不使用Converter
        noConverterTest();
        // 类型不同,使用Converter
        converterTest();

    }

    /**
     * 属性名称、类型都相同(部分属性不拷贝)
     */
    private static void normalCopy() {
        // 模拟查询出数据
        UserDO userDO = DataUtil.createData();
        log.info("拷贝前：userDO:{}", userDO);
        // 第一个参数：源对象， 第二个参数：目标对象，第三个参数：是否使用自定义转换器（下面会介绍），下同
        BeanCopier b = BeanCopier.create(UserDO.class, UserDTO.class, false);
        UserDTO userDTO = new UserDTO();
        b.copy(userDO, userDTO, null);
        log.info("拷贝后：userDTO:{}", userDTO);
    }

    /**
     * 属性名称相同、类型不同(部分属性不拷贝)
     */
    private static void sameNameDifferentType() {
        // 模拟查询出数据
        UserDO userDO = DataUtil.createData();
        log.info("拷贝前：userDO:{}", userDO);

        BeanCopier b = BeanCopier.create(UserDO.class, UserEntity.class, false);
        UserEntity userEntity = new UserEntity();
        b.copy(userDO, userEntity, null);
        log.info("拷贝后：userEntity:{}", userEntity);
    }

    /**
     * 类型不同,不使用Converter
     */
    public static void noConverterTest() {
        // 模拟查询出数据
        UserDO userDO = DataUtil.createData();
        log.info("拷贝前：userDO:{}", userDO);
        BeanCopier copier = BeanCopier.create(UserDO.class, UserDomain.class, false);
        UserDomain userDomain = new UserDomain();
        copier.copy(userDO, userDomain, null);
        log.info("拷贝后：userDomain:{}", userDomain);
    }
    /**
     * 类型不同,使用Converter
     */
    public static void converterTest() {
        // 模拟查询出数据
        UserDO userDO = DataUtil.createData();
        log.info("拷贝前：userDO:{}", userDO);
        BeanCopier copier = BeanCopier.create(UserDO.class, UserDomain.class, true);
        UserConverter converter = new UserConverter();
        UserDomain userDomain = new UserDomain();
        copier.copy(userDO, userDomain, converter);
        log.info("拷贝后：userDomain:{}", userDomain);
    }

}
