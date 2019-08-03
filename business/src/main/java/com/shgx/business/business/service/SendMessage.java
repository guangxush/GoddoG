package com.shgx.business.business.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


/**
 * @author: guangxush
 * @create: 2019/08/03
 */
@Service
@Slf4j
public class SendMessage {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Value("topic")
    private String topic;

    public boolean send(String message){
        try{
            String result = kafkaTemplate.send("topic.business.shgx", message).get().toString();
            if(result!=null){
                return true;
            }

        }catch (Exception e){
            return false;
        }
        return false;
    }

}
