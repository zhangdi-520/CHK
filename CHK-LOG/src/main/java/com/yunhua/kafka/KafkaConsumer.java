package com.yunhua.kafka;

import com.alibaba.fastjson.JSON;
import com.yunhua.constant.KafkaConstant;
import com.yunhua.domain.WebLog;
import com.yunhua.service.WebLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
@Slf4j
public class KafkaConsumer {
    private final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);


    @Autowired
    private WebLogService webLogService;

    //不指定group，默认取yml里配置的
    @KafkaListener(topics = KafkaConstant.WEBLOG,
            groupId = "Web-A-consumer-group-" + KafkaConstant.WEBLOG)
    public void onMessage1(ConsumerRecord<?, ?> consumerRecord) throws IOException {
        Optional<?> optional = Optional.ofNullable(consumerRecord.value());
        if (optional.isPresent()) {
            Object msg = optional.get();
            logger.info("message:{}", msg);
            //webLogService.insertWebLog(JSON.parseObject((String) msg,WebLog.class));

            //执行之前一定确保这个索引存在，不然会报错
            webLogService.insertWebLogIntoES(JSON.parseObject((String) msg,WebLog.class) , KafkaConstant.WEBLOG);
        }
    }

}
