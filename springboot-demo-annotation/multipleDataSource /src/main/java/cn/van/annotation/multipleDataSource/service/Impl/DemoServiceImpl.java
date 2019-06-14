package cn.van.annotation.multipleDataSource.service.Impl;

import cn.van.annotation.multipleDataSource.annotation.DS;
import cn.van.annotation.multipleDataSource.config.MultipleDBConfig;
import cn.van.annotation.multipleDataSource.service.DemoService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DemoServiceImpl implements DemoService {
    @Resource
    private JdbcTemplate jdbcTemplate;

    @DS(value = MultipleDBConfig.SLAVES)
    public List<Map<String, Object>> selectList() {
        List<Map<String, Object>> maps = new ArrayList();
        maps = jdbcTemplate.queryForList("select * from user");
        System.out.println("查询");
        return maps;
    }

    @DS(value = MultipleDBConfig.MASTER)
    public List<Map<String, Object>> selectMaster() {
        List<Map<String, Object>> maps = new ArrayList();
        maps = jdbcTemplate.queryForList("select * from user");
        System.out.println("查询");
        return maps;
    }
    @DS(value = MultipleDBConfig.SLAVES)
    public List<Map<String, Object>> selectSlave() {
        List<Map<String, Object>> maps = new ArrayList();
        maps = jdbcTemplate.queryForList("select * from user");
        System.out.println("查询");
        return maps;
    }

    public Object add() {
        System.out.println("添加");
        return null;
    }


    public Object update() {
        System.out.println("修改");
        return null;
    }


    public Object delete() {
        System.out.println("删除");
        return null;
    }
}
