package cn.van.annotation.multipleDataSource.service;

import java.util.List;
import java.util.Map;

public interface DemoService {
    public List<Map<String,Object>> selectMaster();
    public List<Map<String,Object>> selectSlave();
}
