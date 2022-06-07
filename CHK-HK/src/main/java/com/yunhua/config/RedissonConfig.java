package com.yunhua.config;

import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 分布式锁的简单配置，后期可能会做成集群模式
 */
@Configuration
public class RedissonConfig {

    @Bean
    public Redisson redisson(){
        Config config = new Config();
        config.useSingleServer().setAddress("redis://192.168.229.3:6379").setDatabase(0);
        //更改看门狗的默认过期时间
        config.setLockWatchdogTimeout(10000L);
        return (Redisson)Redisson.create(config);
    }

}
