package cn.van.annotation.multipleDataSource.service.Impl;

import cn.van.annotation.multipleDataSource.annotation.DS;
import cn.van.annotation.multipleDataSource.config.MultipleDBConfig;
import cn.van.annotation.multipleDataSource.enums.DBEnum;
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

    @DS(value = DBEnum.SLAVES)
    public List<Map<String, Object>> selectList() {
        List<Map<String, Object>> maps = new ArrayList();
        maps = jdbcTemplate.queryForList("select * from user");
        return maps;
    }

    @DS(value = DBEnum.MASTER)
    public List<Map<String, Object>> selectMaster() {
        List<Map<String, Object>> maps = new ArrayList();
        maps = jdbcTemplate.queryForList("select * from user");
        return maps;
    }
    @DS(value = DBEnum.SLAVES)
    public List<Map<String, Object>> selectSlave() {
        List<Map<String, Object>> maps = new ArrayList();
        maps = jdbcTemplate.queryForList("select * from user");
        return maps;
    }
}
