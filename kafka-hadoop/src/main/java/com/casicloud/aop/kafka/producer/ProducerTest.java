package com.casicloud.aop.kafka.producer;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.kafka.support.KafkaHeaders;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.gson.Gson;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-producer.xml")
public class ProducerTest {
	@Autowired
    @Qualifier("inputToKafka")
    MessageChannel channel;
	
	@Test
	public void send() throws Exception{
		for (Map<Object, Object> data : DataUtils.grenData(10l)) {
			Message<String> msg = MessageBuilder.withPayload(new Gson().toJson(data))
					.setHeader(KafkaHeaders.TOPIC, "IOT_DATA").build();
			channel.send(msg);
			//System.out.println(data.toString());
		}
	}
}
