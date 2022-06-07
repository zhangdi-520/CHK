package com.yunhua;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;


/**
 * @version V1.0
 * @program: CHK
 * @description: 启动类
 * @author: Mr.Zhang
 * @create: 2022-05-07 14:40
 **/
@EnableAspectJAutoProxy//开启aspetJ动态代理
@EnableCaching
@EnableAsync
@SpringBootApplication
@ServletComponentScan
@EnableDiscoveryClient
@MapperScan(basePackages = "com.yunhua.mapper")
public class UserApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}
