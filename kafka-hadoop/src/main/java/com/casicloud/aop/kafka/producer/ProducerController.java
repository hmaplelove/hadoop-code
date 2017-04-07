package com.casicloud.aop.kafka.producer;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.kafka.support.KafkaHeaders;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

@Controller
public class ProducerController {
	@Autowired
    @Qualifier("inputToKafka")
    MessageChannel channel;
	@RequestMapping("/iotData")
	@ResponseBody
	public Boolean  send() throws Exception{
		boolean flag=true;
		for (Map<Object, Object> data : DataUtils.grenData(100l)) {
			Message<String> msg = MessageBuilder.withPayload(new Gson().toJson(data))
					//.setHeader(KafkaHeaders.MESSAGE_KEY, data.get("key").toString())
					//.setHeader(KafkaHeaders.PARTITION_ID, IotPartitioner.keyMap.get(data.get("key")))
					.setHeader(KafkaHeaders.TOPIC, "IOT_DATA").build();
			boolean b=channel.send(msg);
			if (!b) {
				flag=b;
			}
			//System.out.println(data.toString());
		}
		return flag;
		
	}
}
