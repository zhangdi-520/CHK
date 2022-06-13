import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.yunhua.OrderApplication;
import com.yunhua.domin.DelayQueueRo;
import com.yunhua.kafka.serialization.JsonSerializer;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutionException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = OrderApplication.class)
public class DelayQueueTest {

    @Autowired
    private KafkaTemplate<Object,Object> kafkaTemplate;
    private KafkaConsumer<String, String> consumer;
    private KafkaProducer<String, String> producer;

    private volatile Boolean exit = false;
    private final Object lock = new Object();
//    private final String servers = "172.17.0.6:9092";
    private final String servers = "127.0.0.1:9092";


    @Test
    public void test(){
        DelayQueueRo delayQueueRo = new DelayQueueRo();
        delayQueueRo.setTopic("target");
        delayQueueRo.setKey("key1");
        delayQueueRo.setValue("value1");
        for (int i = 0 ; i<= 10; i++) {
            kafkaTemplate.send("delay-minutes-1", JSON.toJSONString(delayQueueRo));
        }
    }

    @BeforeEach
    public void initConsumer() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "d");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, "5000");
        consumer = new KafkaConsumer<>(props, new StringDeserializer(), new StringDeserializer());
    }

    @BeforeEach
    public void initProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producer = new KafkaProducer<>(props);
    }

 
    @Test
    public void testDelayQueue() throws JsonProcessingException, InterruptedException {
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
                ConsumerRecords<String,String> consumerRecords = consumer.poll(Duration.ofMillis(200));
 
                if (consumerRecords.isEmpty()) {
                    lock.wait();
                    continue;
                }
 
                boolean timed = false;
                for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
                    long timestamp = consumerRecord.timestamp();
                    TopicPartition topicPartition = new TopicPartition(consumerRecord.topic(), consumerRecord.partition());
                    //消费一分钟内信息
                    if (timestamp + 60 * 1000 < System.currentTimeMillis()) {
                        String value = consumerRecord.value();
                        System.out.println(value);
                        DelayQueueRo delayQueueRo = JSON.parseObject(JSON.parse(value).toString(),DelayQueueRo.class);
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
                        System.out.println("===================appTopic:"+appTopic);
                        // send to application topic
                        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(appTopic, appKey, value);
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