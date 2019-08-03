package com.shgx.common.common.kafka.listener.listener;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.adapter.RecordFilterStrategy;
import org.springframework.stereotype.Component;

/**
 * @author: guangxush
 * @create: 2019/08/03
 */
@Component
public class FilterListener {

    private static final Logger log= LoggerFactory.getLogger(TaskListener.class);

    @Autowired
    private ConsumerFactory consumerFactory;

    @Bean
    public ConcurrentKafkaListenerContainerFactory filterContainerFactory() {
        ConcurrentKafkaListenerContainerFactory factory = new ConcurrentKafkaListenerContainerFactory();
        factory.setConsumerFactory(consumerFactory);
        //配合RecordFilterStrategy使用，被过滤的信息将被丢弃
        factory.setAckDiscarded(true);
        factory.setRecordFilterStrategy(new RecordFilterStrategy() {
            @Override
            public boolean filter(ConsumerRecord consumerRecord) {
                long data = Long.parseLong((String) consumerRecord.value());
                log.info("filterContainerFactory filter : "+data);
                if (data % 2 == 0) {
                    return false;
                }
                //返回true将会被丢弃
                return true;
            }
        });
        return factory;
    }

    @KafkaListener(id = "filterCons", topics = "topic.quick.filter",containerFactory = "filterContainerFactory")
    public void filterListener(String data) {
        //这里做数据持久化的操作
        log.error("topic.quick.filter receive : " + data);
    }
}
