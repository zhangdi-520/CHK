package com.yunhua.config;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * spring缓存配置类
 */
@EnableConfigurationProperties({CacheProperties.class})
@Configuration
@EnableCaching
public class SpringCacheConfig {

    @Autowired
    CacheProperties redisProperties;

    @Bean
    RedisCacheConfiguration redisCacheConfiguration(){
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
        //配置key的序列化方式
        config = config.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()));
        //配置value的序列化方式
        config = config.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericFastJsonRedisSerializer()));
        if (redisProperties.getRedis().getTimeToLive() != null) {
            config = config.entryTtl(redisProperties.getRedis().getTimeToLive());
        }

        if (redisProperties.getRedis().getKeyPrefix() != null) {
            config = config.prefixCacheNameWith(redisProperties.getRedis().getKeyPrefix());
        }

        if (!redisProperties.getRedis().isCacheNullValues()) {
            config = config.disableCachingNullValues();
        }

        if (!redisProperties.getRedis().isUseKeyPrefix()) {
            config = config.disableKeyPrefix();
        }
        return config;
    }
}
