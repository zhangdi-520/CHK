package com.yunhua.config;

import com.yunhua.handler.ThreadPoolRejectHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @version V1.0
 * @program: CHK
 * @description: 线程池配置类
 * @author: Mr.Zhang
 * @create: 2022-05-11 10:28
 **/
@Configuration
public class NativeAsyncScheduledTaskConfig implements AsyncConfigurer {

    private static final Logger LOGGER = LoggerFactory.getLogger(NativeAsyncScheduledTaskConfig.class);

    @Autowired
    private ThreadPoolRejectHandler threadPoolRejectHandler;


    @Autowired
    private TaskThreadPoolConfig config;

    /**
     * 1线程池创建，准备好core数量的核心线程，准备接受任务
     * 				1.1.core满了，就将再进来的任务放入阻塞队列，空闲的core自己去队列获取任务
     * 				1.2.阻塞队列满了，直接开新线程执行，最大只能开到max指定的数量
     * 				1.3.max满了，就调用rejectedExecutionHandler拒绝任务
     * 				1.4。max都执行完成，有很多空闲，再指定时间keepAliveTime后，释放max-core这些线程
     * @return
     */
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //最大线程数
        executor.setMaxPoolSize(config.getMaxPoolSize());
        //核心线程数
        executor.setCorePoolSize(config.getCorePoolSize());
        //任务队列的大小
        executor.setQueueCapacity(config.getQueueCapacity());
        //线程池名的前缀
        executor.setThreadNamePrefix(config.getThreadNamePrefix());
        //允许线程的空闲时间30秒
        executor.setKeepAliveSeconds(config.getKeepAliveSeconds());
        //设置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean
        executor.setWaitForTasksToCompleteOnShutdown(true);
        //设置线程池中任务的等待时间，如果超过这个时候还没有销毁就强制销毁，以确保应用最后能够被关闭，而不是阻塞住
        executor.setAwaitTerminationSeconds(config.getAwaitTerminationSeconds());

        /**
         * 拒绝处理策略
         * CallerRunsPolicy()：交由调用方线程运行，比如 main 线程。
         * AbortPolicy()：直接抛出异常。
         * DiscardPolicy()：直接丢弃。
         * DiscardOldestPolicy()：丢弃队列中最老的任务。
         */
        /**
         * 特殊说明：
         * 1. 这里演示环境，拒绝策略咱们采用抛出异常
         * 2.真实业务场景会把缓存队列的大小会设置大一些，
         * 如果，提交的任务数量超过最大线程数量或将任务环缓存到本地、redis、mysql中,保证消息不丢失
         * 3.如果项目比较大的话，异步通知种类很多的话，建议采用MQ做异步通知方案
         */
        //executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());

        //使用自定义的拒绝策略
        executor.setRejectedExecutionHandler(threadPoolRejectHandler);
        //线程初始化
        executor.initialize();
        return executor;
    }

    /**
     * 异步任务重异常处理
     *
     * @return
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (ex, method, params) -> {
            LOGGER.error(ex.getMessage(), ex);
            LOGGER.error("excetion method:{}", method.getName());
        };
    }


}
