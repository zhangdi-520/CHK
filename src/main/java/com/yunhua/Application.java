package com.yunhua;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.apache.shardingsphere.shardingjdbc.spring.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;


/**
 * @version V1.0
 * @program: CHK
 * @description: 启动类
 * @author: Mr.Zhang
 * @create: 2022-05-07 14:40
 **/
@EnableAsync
@SpringBootApplication(exclude = {SpringBootConfiguration.class})
@ServletComponentScan
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
