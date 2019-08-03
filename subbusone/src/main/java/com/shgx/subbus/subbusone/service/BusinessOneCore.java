package com.shgx.subbus.subbusone.service;

import com.alibaba.fastjson.JSONObject;
import com.shgx.common.common.kafka.listener.listener.AckListener;
import com.shgx.subbus.subbusone.model.BusinessOne;
import com.shgx.subbus.subbusone.model.BusinessOneVO;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: guangxush
 * @create: 2019/08/03
 */
@Component
public class BusinessOneCore {

    private static final Logger log= LoggerFactory.getLogger(AckListener.class);

    @Autowired
    private BusinessOneService businessOneService;

    private Map<String, Object> consumerProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return props;
    }

    @Bean("ackContainerFactory")
    public ConcurrentKafkaListenerContainerFactory ackContainerFactory() {
        ConcurrentKafkaListenerContainerFactory factory = new ConcurrentKafkaListenerContainerFactory();
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory(consumerProps()));
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory(consumerProps()));
        return factory;
    }

    @KafkaListener(id = "ack", topics = "topic.business.shgx",containerFactory = "ackContainerFactory")
    public void ackListener(ConsumerRecord record, Acknowledgment ack) {
        log.info("topic.business.shgx : " + record.value());
        Object object = JSONObject.parseObject(record.value().toString(), BusinessOneVO.class);
        BusinessOneVO businessOneVO = new BusinessOneVO();
        if(object!=null){
            try {
                BeanUtils.copyProperties(object, businessOneVO);
                log.info("origin:"+object.toString());
                log.info("target:"+businessOneVO.toString());
            } catch (Exception e) {
                log.error("prase the object failed : " + e);
            }
            BusinessOne businessOne = BusinessOne.builder()
                    .uid(businessOneVO.getUid())
                    .money(businessOneVO.getMoney())
                    .status(businessOneVO.getStatus())
                    .build();
            businessOneService.saveBusinessOne(businessOne);
        }
        ack.acknowledge();
    }
}
