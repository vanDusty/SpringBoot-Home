package cn.van.annotation.multipleDataSource.controller;

import cn.van.annotation.multipleDataSource.service.DemoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("demo")
public class DemoController {
    @Resource
    private DemoService demoService;
    @GetMapping("selectMaster")
    public Object selectMaster(){
        return demoService.selectMaster();
    }

    @GetMapping("selectSlave")
    public Object selectSlave(){
        return demoService.selectSlave();
    }
}
