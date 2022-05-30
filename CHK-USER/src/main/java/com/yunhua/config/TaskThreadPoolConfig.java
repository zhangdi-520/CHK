package com.yunhua.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @version V1.0
 * @program: CHK
 * @description: 线程池配置类
 * @author: Mr.Zhang
 * @create: 2022-05-11 10:19
 **/
@Data
@Component
@ConfigurationProperties(prefix = "task.pool")
public class TaskThreadPoolConfig {
    private int corePoolSize;
    private int maxPoolSize;
    private int keepAliveSeconds;
    private int queueCapacity;
    private int awaitTerminationSeconds;
    private String threadNamePrefix;

}
