package com.casicloud.aop.kafka.core.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.casicloud.aop.kafka.core.service.KafkaService;
import com.casicloud.aop.mongodb.iot.IotDataService;
import com.casicloud.aop.mongodb.iot.model.IotData;
import com.google.gson.Gson;

public class KafkaMongoService implements KafkaService{
	private static final Logger logger = LoggerFactory.getLogger(KafkaMongoService.class);
	private static Gson gson=new Gson();
	@Autowired
	private IotDataService iotDataService;
	@Override
	public void processMessage(Map<Object, Map<Object, Object>> message) throws Exception {
		onMessage(message);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void onMessage(Map<Object, Map<Object, Object>> message) throws Exception {

		for (Map.Entry < Object,Map<Object, Object>>entry:  message.entrySet()){
			logger.info("Suchit Topic:" + entry.getKey());
            for (Entry<Object, Object> msg : entry.getValue().entrySet()) {
            	List<String> list=(List<String>) msg.getValue();
            	logger.info("key=========>"+msg.getKey());
            	for (String json : list) {
            		logger.info(gson.toJson(json));
            		HashMap params=gson.fromJson(json, HashMap.class);
            		IotData iotData=new IotData();
            		BeanUtils.populate(iotData, params);
            		iotDataService.save(iotData);
				}
            }
        }
	
	}

}
