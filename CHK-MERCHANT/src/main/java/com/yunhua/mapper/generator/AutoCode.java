package com.yunhua.mapper.generator;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.ArrayList;
import java.util.List;

class AutoCode {
    public static void main(String[] args) {
        // 1、全局配置
        GlobalConfig globalConfig = new GlobalConfig();//构建全局配置对象
        String projectPath = System.getProperty("user.dir")+"/CHK-MERCHANT";// 获取当前用户的目录
        globalConfig
                .setOutputDir(projectPath + "/src/main/java")// 输出文件路径
                .setAuthor("魏启恒")// 设置作者名字
                .setOpen(true)// 是否打开资源管理器
                .setFileOverride(true)// 是否覆盖原来生成的
                .setIdType(IdType.INPUT)// 主键策略
                .setDateType(DateType.ONLY_DATE)
                .setBaseResultMap(true)// 生成resultMap
                .setSwagger2(true)
                .setServiceName("%sService");// 生成的service接口名字首字母是否为I，这样设置就没有I

        // 2、数据源配置
        DataSourceConfig dataSourceConfig = new DataSourceConfig();// 创建数据源配置
        dataSourceConfig
                .setUrl("jdbc:mysql://192.168.229.3:3307/yunhua-chk?userSSL=false&useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC")
                .setDriverName("com.mysql.cj.jdbc.Driver")
                .setUsername("root")
                .setPassword("admin123456")
                .setDbType(DbType.MYSQL);

        // 3、包配置

//        TemplateConfig templateConfig = new TemplateConfig();
//        templateConfig.setController("");
//        templateConfig.setXml("");
//        templateConfig.setService("");
//        templateConfig.setServiceImpl("");

        PackageConfig packageConfig = new PackageConfig();
        packageConfig
                .setParent("com.yunhua")
                .setEntity("entity")
                .setService("service")
                .setServiceImpl("service.impl")
                .setController("controller")
//                .setController("controller")
//                .setService("service")
                .setMapper("mapper");


        // 4、策略配置
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig
                .setCapitalMode(true)// 开启全局大写命名
                .setInclude("chk_merchant_employee")// 设置要映射的表
                .setNaming(NamingStrategy.underline_to_camel)// 下划线到驼峰的命名方式
                .setColumnNaming(NamingStrategy.underline_to_camel)// 下划线到驼峰的命名方式
                .setEntityLombokModel(true);// 是否使用lombok
        //.setLogicDeleteFieldName()




        // 5、自定义配置（配置输出xml文件到resources下）
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };
        List<FileOutConfig> focList = new ArrayList<>();
        String templatePath = "/templates/mapper.xml.vm";
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + "/src/main/resources/mappers/"
                        + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        cfg.setFileOutConfigList(focList);

        // 6、整合配置
        AutoGenerator autoGenerator = new AutoGenerator();// 构建代码生自动成器对象
        autoGenerator
                .setGlobalConfig(globalConfig)// 将全局配置放到代码生成器对象中
                .setDataSource(dataSourceConfig)// 将数据源配置放到代码生成器对象中
                .setPackageInfo(packageConfig)// 将包配置放到代码生成器对象中
                .setStrategy(strategyConfig)// 将策略配置放到代码生成器对象中
                .setCfg(cfg)// 将自定义配置放到代码生成器对象中
                //.setTemplate(templateConfig)
                .execute();// 执行！
    }
}