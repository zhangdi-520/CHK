package com.yunhua;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
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
@SpringBootApplication
@MapperScan("com.yunhua.mapper")
@ServletComponentScan
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
