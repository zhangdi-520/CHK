package com.yunhua;

import org.apache.shardingsphere.shardingjdbc.spring.boot.SpringBootConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
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
@SpringBootApplication(exclude = {SpringBootConfiguration.class})
@ServletComponentScan
public class UserApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}
