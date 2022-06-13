package com.yunhua.controller;


import com.alibaba.fastjson.JSON;
import com.yunhua.domin.DelayQueueRo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class TestController {

    @Autowired
    private KafkaTemplate<Object,Object> kafkaTemplate;

    @GetMapping("index")
    public String index() {
        DelayQueueRo delayQueueRo = new DelayQueueRo();
        delayQueueRo.setTopic("target");
        delayQueueRo.setKey("key1");
        delayQueueRo.setValue("value1");
        for (int i = 0 ; i<= 10; i++) {
            kafkaTemplate.send("delay-minutes-1", JSON.toJSONString(delayQueueRo));
        }
        return "index";
    }
}
