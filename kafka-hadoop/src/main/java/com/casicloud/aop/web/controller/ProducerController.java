package com.casicloud.aop.web.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.kafka.support.KafkaHeaders;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.casicloud.aop.kafka.core.service.impl.KafkaMongoService;
import com.casicloud.aop.mongodb.iot.model.IotData;
import com.google.gson.Gson;

@Controller
public class ProducerController {
	private static final Logger logger = LoggerFactory.getLogger(KafkaMongoService.class);
	private static Gson gson=new Gson();
	@Autowired
    @Qualifier("inputToKafka")
    MessageChannel channel;
	@RequestMapping("/iotData")
	@ResponseBody
	public Boolean send(@RequestBody List<IotData> datas) throws Exception{
		boolean flag=true;
		for (IotData data : datas) {
			String json=gson.toJson(data);
			Message<String> msg = MessageBuilder.withPayload(json)
					//.setHeader(KafkaHeaders.MESSAGE_KEY, data.get("key").toString())
					//.setHeader(KafkaHeaders.PARTITION_ID, IotPartitioner.keyMap.get(data.get("key")))
					.setHeader(KafkaHeaders.TOPIC, "IOT_DATA").build();
			boolean b=channel.send(msg);
			if (!b) {
				flag=b;
			}
			logger.info(json);
		}
		return flag;
		
	}
}
