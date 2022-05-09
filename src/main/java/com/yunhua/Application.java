package com.yunhua;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * @version V1.0
 * @program: CHK
 * @description: TODO
 * @author: Mr.Zhang
 * @create: 2022-05-07 14:40
 **/
@SpringBootApplication
@MapperScan("com.yunhua.mapper")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
