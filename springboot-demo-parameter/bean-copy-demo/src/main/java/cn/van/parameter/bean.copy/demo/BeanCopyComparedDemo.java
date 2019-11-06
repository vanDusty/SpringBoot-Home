package cn.van.parameter.bean.copy.demo;

import cn.van.parameter.bean.copy.entity.UserDO;
import cn.van.parameter.bean.copy.model.UserDTO;
import cn.van.parameter.bean.copy.util.DataUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.beans.BeanCopier;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: BeanCopyComparedDemo
 *
 * @author: Van
 * Date:     2019-11-02 17:26
 * Description: ${DESCRIPTION}
 * Version： V1.0
 */
@Slf4j
public class BeanCopyComparedDemo {
    /**
     * 缓存 BeanCopier
     */
    private static final ConcurrentHashMap<String, BeanCopier> BEAN_COPIERS = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        beanUtil();
        beanCopier();
        beanCopierWithCache();
    }


    private static void beanUtil() {
        // 工具类生成10w条数据
        List<UserDO> list = DataUtil.createDataList(10000);
        long start = System.currentTimeMillis();
        List<UserDTO> dtoList = new ArrayList<>();
        list.forEach(userDO -> {
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(userDO,userDTO);
            dtoList.add(userDTO);
        });
        log.info("BeanUtils cotTime: {}ms", System.currentTimeMillis() - start);
    }

    private static void beanCopier() {
        // 工具类生成10w条数据
        List<UserDO> doList = DataUtil.createDataList(10000);
        long start = System.currentTimeMillis();
        List<UserDTO> dtoList = new ArrayList<>();
        doList.forEach(userDO -> {
            BeanCopier b = BeanCopier.create(UserDO.class, UserDTO.class, false);
            UserDTO userDTO = new UserDTO();
            b.copy(userDO, userDTO, null);
            dtoList.add(userDTO);
        });
        log.info("BeanCopier costTime: {}ms", System.currentTimeMillis() - start);
    }

    /**
     * 加缓存
     */
    private static void beanCopierWithCache() {

        List<UserDO> userDOList = DataUtil.createDataList(10000);
        long start = System.currentTimeMillis();
        List<UserDTO> userDTOS = new ArrayList<>();
        userDOList.forEach(userDO -> {
            UserDTO userDTO = new UserDTO();
            copy(userDO, userDTO);
            userDTOS.add(userDTO);
        });
        log.info("BeanCopier 加缓存后 costTime: {}ms", System.currentTimeMillis() - start);

    }

    public static void copy(Object srcObj, Object destObj) {
        String key = genKey(srcObj.getClass(), destObj.getClass());
        BeanCopier copier = null;
        if (!BEAN_COPIERS.containsKey(key)) {
            copier = BeanCopier.create(srcObj.getClass(), destObj.getClass(), false);
            BEAN_COPIERS.put(key, copier);
        } else {
            copier = BEAN_COPIERS.get(key);
        }
        copier.copy(srcObj, destObj, null);

    }
    private static String genKey(Class<?> srcClazz, Class<?> destClazz) {
        return srcClazz.getName() + destClazz.getName();
    }
}
