package com.shgx.business.business.kafka;

import com.shgx.business.business.service.KafkaSendResultHandler;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author: guangxush
 * @create: 2019/08/03
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class SendMessTest {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Autowired
    private KafkaSendResultHandler producerListener;

    @Resource
    private KafkaTemplate defaultKafkaTemplate;

    @Test
    public void testDemo() throws InterruptedException {
        kafkaTemplate.send("topic.business.shgx", "this is my first test data");
        //休眠5秒，为了使监听器有足够的时间监听到topic的数据
        Thread.sleep(5000);
    }

    @Test
    public void testDefaultKafkaTemplate() {
        defaultKafkaTemplate.sendDefault("I`m send msg to default topic");
    }

    @Test
    public void testTemplateSend() {
        //发送带有时间戳的消息
        kafkaTemplate.send("topic.business.shgx", 0, System.currentTimeMillis(), 0, "send message with timestamp");

        //使用ProducerRecord发送消息
        ProducerRecord record = new ProducerRecord("topic.business.shgx", "use ProducerRecord to send message");
        kafkaTemplate.send(record);

        //使用Message发送消息
        Map map = new HashMap();
        map.put(KafkaHeaders.TOPIC, "topic.business.shgx");
        map.put(KafkaHeaders.PARTITION_ID, 0);
        map.put(KafkaHeaders.MESSAGE_KEY, 0);
        GenericMessage message = new GenericMessage("use Message to send message", new MessageHeaders(map));
        kafkaTemplate.send(message);
    }

    @Test
    public void testProducerListen() throws InterruptedException {
        kafkaTemplate.setProducerListener(producerListener);
        kafkaTemplate.send("topic.business.shgx", "test producer listen");
        Thread.sleep(1000);
    }

    // 异步消息
    @Test
    public void testSyncSend() throws ExecutionException, InterruptedException {
        kafkaTemplate.send("topic.business.shgx", "test sync send message").get();
    }

    // 异步超时消息
    @Test
    public void testTimeOut() throws ExecutionException, InterruptedException, TimeoutException {
        kafkaTemplate.send("topic.business.shgx", "test send message timeout").get(100, TimeUnit.MICROSECONDS);
    }
}
