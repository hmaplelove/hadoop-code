package com.casicloud.aop.kafka.core.service.impl;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.casicloud.aop.kafka.core.service.KafkaService;
import com.casicloud.aop.mongodb.core.MongoBase;
import com.google.gson.Gson;

public class KafkaMongoService implements KafkaService{
	private static final Logger logger = LoggerFactory.getLogger(KafkaMongoService.class);
	/*@Autowired
	private MongoBase mongoBase;*/
	@Override
	public void processMessage(Map<Object, Map<Object, Object>> message) throws IOException {
		logger.info("MongoDB");
		onMessage(message);
		
		
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void onMessage(Map<Object, Map<Object, Object>> message) throws IOException {
		//mongoBase.save()
		System.out.println(new Gson().toJson(message));
	}

}
