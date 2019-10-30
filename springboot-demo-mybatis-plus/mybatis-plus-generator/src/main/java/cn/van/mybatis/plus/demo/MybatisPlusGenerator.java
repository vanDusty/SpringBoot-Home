package cn.van.mybatis.plus.demo;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.AbstractTemplateEngine;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Copyright (C), 2015-2019, 风尘博客
 * 公众号 : 风尘博客
 * FileName: MybatisPlusGenerator
 *
 * @author: Van
 * Date:     2019-10-08 20:46
 * Description: 代码生成器
 * Version： V1.0
 */
@Slf4j
public class MybatisPlusGenerator {

    /**
     * 作者名
     */
    static final String author = "Van";
    /**
     * 模块名称
     */
    static final String moduleName = "mybatis.plus.demo";
    /**
     * 生成代码的位置
     */
    static final String projectPath = "/Users/zhangfan/Documents/Github-van/SpringBoot-Home/springboot-demo-mybatis-plus/mybatis-plus-generator/";
    /**
     * 数据库相关配置
     */
    static final String dbUrl = "jdbc:mysql://47.98.178.84:3306/system";

    static final String dbUserName = "system";

    static final String dbPassWord = "password";
    /**
     * 所需生成的的表
     */
    static String[] tableNames = {"sys_user","sys_role"};

    public static void main(String[] args) {

        if (true) {
            Cfg cfg = new Cfg();
            cfg.setTableNames(tableNames);
            cfg.setProjectPath(projectPath);
            // 生成mapper文件
            cfg.setNeedToGenMapperXml(true);
            // 不生成service
            cfg.setNeedToGenService(false);
            // 不生成controller
            cfg.setNeedToGenController(false);
            gen(cfg);
        }

    }

    private static void gen(Cfg cfg) {
        // 代码生成器
        ExtendedAutoGenerator mpg = new ExtendedAutoGenerator();

        // =============================全局配置===============================
        GlobalConfig gc = new GlobalConfig()
                // 文件覆盖
                .setFileOverride(true)
                // 生成文件的位置
                .setOutputDir(projectPath+ "/src/main/java")
                // 主键生成策略 生成BaseResultMap和BaseColumnList
                .setIdType(IdType.AUTO).setBaseResultMap(false).setBaseColumnList(false)
                // 指定作者
                .setAuthor(author)
                // 设置Controller、Service、ServiceImpl、Dao、Mapper文件名称，%s是依据表名转换来的
//                .setControllerName("%sController").setServiceName("MP%sService").setServiceImplName("%sServiceImpl")
                // 设置Dao、Mapper文件名称，%s是依据表名转换来的
                .setMapperName("%sMapper").setXmlName("%sMapper").setEntityName("%sDO")
                // 生成swagger字段说明
//                .setSwagger2(true)
                .setOpen(false).setKotlin(false);
        mpg.setGlobalConfig(gc);
        // ================================数据源配置============================
        DataSourceConfig dsc = new DataSourceConfig();
        // 用户名、密码、驱动、url
        dsc.setDbType(DbType.MYSQL).setDriverName("com.mysql.jdbc.Driver")
                .setUrl(dbUrl).setUsername(dbUserName).setPassword(dbPassWord);
        mpg.setDataSource(dsc);

        // ===============================包名策略配置：父包.模块.controller===============================
        PackageConfig pc = new PackageConfig();
        // 父包名 模块名
        pc.setParent("cn.van").setModuleName(moduleName)
                // 分层包名
//      .setController("controller").setService("service").setServiceImpl("service.impl")
                .setEntity("entity").setMapper("mapper");
        mpg.setPackageInfo(pc);
        // =====================================策略配置==================================
        StrategyConfig strategy = new StrategyConfig();
        // 数据库表映射到实体的命名策略 user_info -> userInfo
        strategy.setNaming(NamingStrategy.underline_to_camel)
                // 表名 字段名 是否使用下滑线命名
                .setColumnNaming(NamingStrategy.underline_to_camel);
        // 根据lombok 生成实体
        strategy.setEntityLombokModel(true);
        //生成的表
        if (cfg.getTableNames() != null) {
            strategy.setInclude(cfg.getTableNames());
        } else {
            strategy.setInclude(scanner("表名"));
        }
        strategy.setControllerMappingHyphenStyle(true);
        mpg.setStrategy(strategy);
        // =====================================自定义配置==================================
        InjectionConfig injectionConfig = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };
        List<FileOutConfig> focList = new ArrayList();
        if (cfg.isNeedToGenMapperXml()) {
            focList.add(new FileOutConfig("/templates/mapper.xml.ftl") {
                @Override
                public String outputFile(TableInfo tableInfo) {
                    // 自定义输入文件名称
                    return projectPath + "/src/main/resources/mapper/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
                }
            });
        }
        injectionConfig.setFileOutConfigList(focList);
        mpg.setCfg(injectionConfig);
        TemplateConfig templateConfig = new TemplateConfig().setXml(null);
        mpg.setTemplate(templateConfig);
        // 选择 freemarker 引擎需要指定如下加，注意 pom 依赖必须有！
        AbstractTemplateEngine templateEngine = new FreemarkerTemplateEngine();
        mpg.setTemplateEngine(templateEngine);

        // 忽略service跟controller生成，把mpg.execute()方法拆解开自行定制
        //mpg.execute();
        log.debug("==========================准备生成文件...==========================");
        ConfigBuilder configBuilder = new ConfigBuilder(pc, dsc, strategy, templateConfig, gc);
        configBuilder.setInjectionConfig(injectionConfig);
        List<TableInfo> tableInfoList = configBuilder.getTableInfoList();
        for (TableInfo tableInfo : tableInfoList) {
            if (!cfg.isNeedToGenService()) {
                tableInfo.setServiceName(null);
                tableInfo.setServiceImplName(null);
            }
            if (!cfg.isNeedToGenController()) {
                tableInfo.setControllerName(null);
            }
        }
        // 模板引擎初始化执行文件输出
        templateEngine.init(mpg.pretreatmentConfigBuilder(configBuilder)).mkdirs().batchOutput().open();
        log.debug("==========================文件生成完成！！！==========================");

    }
    private static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(("请输入" + tip + "："));
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotEmpty(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }
    private static class ExtendedAutoGenerator extends AutoGenerator {
        @Override
        public ConfigBuilder pretreatmentConfigBuilder(ConfigBuilder config) {
            return super.pretreatmentConfigBuilder(config);
        }
    }

    @Data
    private static class Cfg {
        private String[] tableNames;
        private String projectPath;
        private boolean needToGenMapperXml = false;
        private boolean needToGenService = false;
        private boolean needToGenController = false;
    }
}
