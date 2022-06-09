package com.yunhua.config;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.yunhua.constant.DBTypeEnum;
import io.seata.rm.datasource.DataSourceProxy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.*;
/**
 * 读写分离自动配置类
 */
@Configuration
@EnableConfigurationProperties(MybatisPlusProperties.class)
public class ChkMybatisAutoConfiguration {


    @Bean(name = "masterDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.master")
    public DataSource masterDataSource(){
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "slaveDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.slave")
    public DataSource slaveDataSource(){
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "myRoutingDataSource")
    public DataSource myRoutingDataSource(@Qualifier("masterDataSource") DataSource masterDataSource,
                                          @Qualifier("slaveDataSource") DataSource slaveDataSource){
        Map<Object,Object> targetDataSource = new HashMap<>();
        targetDataSource.put(DBTypeEnum.MASTER,new DataSourceProxy(masterDataSource));
        targetDataSource.put(DBTypeEnum.SLAVE,new DataSourceProxy(slaveDataSource));
        MyRoutingDataSource myRoutingDataSource = new MyRoutingDataSource();
        myRoutingDataSource.setDefaultTargetDataSource(masterDataSource);
        myRoutingDataSource.setTargetDataSources(targetDataSource);
        return myRoutingDataSource;
    }


}
