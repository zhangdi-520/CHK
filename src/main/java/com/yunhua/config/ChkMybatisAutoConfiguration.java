package com.yunhua.config;


import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.baomidou.mybatisplus.autoconfigure.SpringBootVFS;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.apache.shardingsphere.api.config.masterslave.MasterSlaveRuleConfiguration;
import org.apache.shardingsphere.shardingjdbc.api.MasterSlaveDataSourceFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Stream;

/**
 * 读写分离自动配置类
 */
@MapperScan(sqlSessionFactoryRef = "sqlSessionFactoryChk", basePackages = {"com.yunhua.mapper"})
@Configuration
public class ChkMybatisAutoConfiguration {

    private static final ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();

    private final String[] MAPPER_XML_PATH = new String[]{"classpath*:mapper/*.xml"};

    /**
     * 配置主库数据源
     * @return
     */
    @Bean(name = "dataSourceChk")
    @Primary
    @ConfigurationProperties("spring.datasource.master")
    public DataSource dataSourceChk(){
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(name = "dataSourceChkSlave")
    @ConfigurationProperties("spring.datasource.slave")
    public DataSource dataSourceChkSlave(){
        return DruidDataSourceBuilder.create().build();
    }

    @Bean("sqlSessionFactoryChk")
    @Primary
    public SqlSessionFactory sqlSessionFactoryChk() throws Exception {
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setDataSource(getMasterSlaveDataSource());
        factory.setVfs(SpringBootVFS.class);
        factory.setMapperLocations(resolveMapperLocations());
        //开启驼峰
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true);
        factory.setConfiguration(configuration);
        return factory.getObject();
    }

    @Bean(name = "sqlSessionTemplateChk")
    @Primary
    public SqlSessionTemplate sqlSessionTemplateChk() throws Exception {
        return new SqlSessionTemplate(sqlSessionFactoryChk());
    }

    public Resource[] resolveMapperLocations() {
        return Stream.of(Optional.ofNullable(MAPPER_XML_PATH).orElse(new String[0]))
                .flatMap(location -> Stream.of(getResources(location))).toArray(Resource[]::new);
    }

    private Resource[] getResources(String location) {
        try {
            return resourceResolver.getResources(location);
        } catch (IOException e) {
            return new Resource[0];
        }
    }

    private DataSource getMasterSlaveDataSource() throws SQLException {
        MasterSlaveRuleConfiguration masterSlaveRuleConfig = new MasterSlaveRuleConfiguration(
                "chk_ds_master_slave", "dataSourceChk", Arrays.asList("dataSourceChkSlave"));
        return MasterSlaveDataSourceFactory.createDataSource(createDataSourceMap(), masterSlaveRuleConfig, new Properties());
    }

    private Map<String, DataSource> createDataSourceMap() {
        Map<String, DataSource> result = new HashMap<>();
        result.put("dataSourceChk", dataSourceChk());
        result.put("dataSourceChkSlave", dataSourceChkSlave());
        return result;
    }


}
