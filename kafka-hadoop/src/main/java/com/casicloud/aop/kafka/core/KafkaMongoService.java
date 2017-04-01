package com.casicloud.aop.kafka.core;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.casicloud.aop.mongodb.core.MongoBase;

public class KafkaMongoService implements KafkaService{
	@Autowired
	private MongoBase mongoBase;
	@Override
	public void processMessage(Map<Object, Map<Object, Object>> message) throws IOException {
		//mongoBase.save()
	}

}
