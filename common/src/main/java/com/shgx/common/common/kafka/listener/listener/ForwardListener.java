package com.shgx.common.common.kafka.listener.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

/**
 * @author: guangxush
 * @create: 2019/08/03
 */
@Component
public class ForwardListener {

    private static final Logger log= LoggerFactory.getLogger(ForwardListener.class);

    @KafkaListener(id = "forward", topics = "topic.business.shgx")
    @SendTo("topic.business.real")
    public String forward(String data) {
        log.info("topic.business.shgx "+data+" topic.business.real");
        return "topic.business.shgx send msg : " + data;
    }
}
