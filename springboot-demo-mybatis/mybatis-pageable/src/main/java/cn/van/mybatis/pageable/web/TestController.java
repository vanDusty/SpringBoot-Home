package cn.van.mybatis.pageable.web;

import cn.van.mybatis.pageable.entity.UserInfoDO;
import cn.van.mybatis.pageable.interceptor.PageInterceptor;
import cn.van.mybatis.pageable.mapper.UserMapper;
import cn.van.mybatis.pageable.model.PageResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author chenmingming
 * @date 2018/9/21
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Resource
    private UserMapper userMapper;

    @GetMapping("/all")
    public Object all(){
        PageInterceptor.startPage(1,2);
        List<UserInfoDO> all = userMapper.findAll();
        PageResult<UserInfoDO> modelPageResult = new PageResult<>(all);
        return modelPageResult;
    }
}
