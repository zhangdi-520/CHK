package com.yunhua.kafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


/**
 * @version V1.0
 * @program: CHK
 * @description: kakfa延迟队列消费日志
 * @author: Mr.Zhang
 * @create: 2022-05-11 16:14
 **/
@Component
@Slf4j
public class DelayQueueConsumer {


    @KafkaListener(topics = "target",
            groupId = "Web-A-consumer-group-delay")
    public void onMessage(String  message) {
        System.out.println("消费消息："+message);
    }
}
