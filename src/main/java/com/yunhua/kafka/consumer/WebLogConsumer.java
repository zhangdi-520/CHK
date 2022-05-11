package com.yunhua.kafka.consumer;

import com.yunhua.kafka.constant.KafkaConstant;
import com.yunhua.kafka.message.WebLog;
import com.yunhua.mapper.WebLogMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @version V1.0
 * @program: CHK
 * @description: kakfa日志信息消费者
 * @author: Mr.Zhang
 * @create: 2022-05-11 16:14
 **/
@Component
@Slf4j
public class WebLogConsumer {

    @Autowired
    private WebLogMapper webLogMapper;

    @KafkaListener(topics = KafkaConstant.WEBLOG,
            groupId = "Web-A-consumer-group-" + KafkaConstant.WEBLOG)
    public void onMessage(WebLog webLog) {
        if (Objects.isNull(webLog)){
            return;
        }
        webLogMapper.insertWebLog(webLog);
    }
}
