package com.shgx.common.common.kafka.listener.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author: guangxush
 * @create: 2019/08/03
 */
@Component
public class BusinessOneListener {
    private static final Logger log= LoggerFactory.getLogger(BusinessOneListener.class);

    //声明consumerID为demo，监听topicName为topic.quick.demo的Topic
    @KafkaListener(id = "businessone", topics = "topic.shgx.business")
    public void listen(String msgData) {
        log.info("demo receive : "+msgData);
    }
}
