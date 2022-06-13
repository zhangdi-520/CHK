package com.yunhua;


import com.alibaba.fastjson.JSON;
import com.yunhua.domin.DelayQueueRo;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.platform.commons.util.StringUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutionException;

@EnableDiscoveryClient
@SpringBootApplication
@RefreshScope
@EnableTransactionManagement
@MapperScan(basePackages = "com.yunhua.mapper")
public class OrderApplication{

    @Autowired
    private KafkaTemplate kafkaTemplate;

    private final Object lock = new Object();

    private volatile Boolean exit = false;

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }

    @PostConstruct
    public void run() throws Exception {
        Producer producer = kafkaTemplate.getProducerFactory().createProducer();
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "d");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, "5000");
        KafkaConsumer consumer = new KafkaConsumer<>(props, new StringDeserializer(), new StringDeserializer());
        String topic = "delay-minutes-1";
        List<String> topics = Collections.singletonList(topic);
        consumer.subscribe(topics);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                synchronized (lock) {
                    //获取被暂停分区的集合
                    Set<TopicPartition> partitionSet = consumer.paused();
                    //恢复被暂停的分区
                    consumer.resume(partitionSet);
                    lock.notify();
                }
            }
        }, 0, 1000);

        do {

            synchronized (lock) {
                ConsumerRecords<String, Object> consumerRecords = consumer.poll(Duration.ofMillis(200));

                if (consumerRecords.isEmpty()) {
                    lock.wait();
                    continue;
                }

                boolean timed = false;
                for (ConsumerRecord<String, Object> consumerRecord : consumerRecords) {
                    long timestamp = consumerRecord.timestamp();
                    TopicPartition topicPartition = new TopicPartition(consumerRecord.topic(), consumerRecord.partition());
                    //消费一分钟内信息
                    if (timestamp + 60 * 1000 < System.currentTimeMillis()) {
                        Object value = consumerRecord.value();
                        DelayQueueRo delayQueueRo = JSON.parseObject(JSON.parse(String.valueOf(value)).toString(), DelayQueueRo.class);
                        String jsonTopic = delayQueueRo.getTopic();
                        String appTopic = null, appKey = null, appValue = null;

                        if (!StringUtils.isBlank(jsonTopic)) {
                            appTopic = jsonTopic;
                        }
                        if (appTopic == null) {
                            continue;
                        }
                        String jsonNodeKey = delayQueueRo.getKey();
                        if (!StringUtils.isBlank(jsonNodeKey)) {
                            appKey = jsonNodeKey;
                        }

                        String jsonNodeValue = delayQueueRo.getValue();
                        if (!StringUtils.isBlank(jsonNodeValue)) {
                            appValue = jsonNodeValue;
                        }
                        // send to application topic
                        ProducerRecord<String, Object> producerRecord = new ProducerRecord<>(appTopic, appKey, value);
                        try {
                            producer.send(producerRecord).get();
                            // success. commit message
                            OffsetAndMetadata offsetAndMetadata = new OffsetAndMetadata(consumerRecord.offset() + 1);
                            HashMap<TopicPartition, OffsetAndMetadata> metadataHashMap = new HashMap<>();
                            metadataHashMap.put(topicPartition, offsetAndMetadata);
                            //设置偏移量
                            consumer.commitSync(metadataHashMap);
                        } catch (ExecutionException e) {
                            //暂停消费
                            consumer.pause(Collections.singletonList(topicPartition));
                            //重置分区offest
                            consumer.seek(topicPartition, consumerRecord.offset());
                            timed = true;
                            break;
                        }
                    } else {
                        //
                        consumer.pause(Collections.singletonList(topicPartition));
                        consumer.seek(topicPartition, consumerRecord.offset());
                        timed = true;
                        break;
                    }
                }

                if (timed) {
                    lock.wait();
                }
            }
        } while (!exit);
    }
}
