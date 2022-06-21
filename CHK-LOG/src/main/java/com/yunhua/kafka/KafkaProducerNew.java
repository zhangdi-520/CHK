package com.yunhua.kafka;

import com.alibaba.fastjson.JSON;
import com.yunhua.constant.KafkaConstant;
import io.swagger.annotations.Api;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Api(value = "kafka测试接口",tags = "kafka测试")
public class KafkaProducerNew {
    @Resource
    private KafkaTemplate<String, Object> kafkaTemplate;

    @GetMapping("/kafka/test/{msg}")
    public void sendMessage(@PathVariable("msg") String msg) {

        kafkaTemplate.send( KafkaConstant.WEBLOG, JSON.toJSONString(msg));
    }
}
