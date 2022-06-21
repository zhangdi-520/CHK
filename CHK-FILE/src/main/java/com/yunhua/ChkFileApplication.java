package com.yunhua;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableCaching
@EnableAspectJAutoProxy//开启aspetJ动态代理
@EnableAsync
@EnableTransactionManagement
@ServletComponentScan
@MapperScan("com.yunhua.mapper")
@EnableDiscoveryClient
@RefreshScope
@EnableFeignClients
public class ChkFileApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChkFileApplication.class, args);
    }

}
