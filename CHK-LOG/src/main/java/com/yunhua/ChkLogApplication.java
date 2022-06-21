package com.yunhua;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@SpringBootApplication
@MapperScan("com.yunhua.mapper")
@RefreshScope
public class ChkLogApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChkLogApplication.class, args);
    }

}
