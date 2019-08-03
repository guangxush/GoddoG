package com.shgx.business.business.kafka;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DescribeTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;

/**
 * @author: guangxush
 * @create: 2019/08/03
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class CreateParametersTest {

    @Autowired
    private AdminClient adminClient;

    @Test
    public void testCreateTopic() throws InterruptedException {
        NewTopic topic = new NewTopic("topic.business.shgx", 1, (short) 1);
        adminClient.createTopics(Arrays.asList(topic));
        Thread.sleep(1000);
    }

    @Test
    public void testSelectTopicInfo() throws ExecutionException, InterruptedException {
        DescribeTopicsResult result = adminClient.describeTopics(Arrays.asList("topic.business.shgx"));
        result.all().get().forEach((k,v)->System.out.println("k: "+k+" ,v: "+v.toString()+"\n"));
    }
}
