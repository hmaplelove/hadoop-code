package com.casicloud.aop.kafka.core.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.kafka.support.KafkaHeaders;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

import com.casicloud.aop.kafka.core.service.KafkaService;
import com.google.gson.Gson;

public class KafkaStormService implements KafkaService{

	@SuppressWarnings("rawtypes")
	private static Map<String, List<Map>> pressuresMap = new HashMap<String, List<Map>>();
	private static Gson gson=new Gson();
	private static final Logger logger = LoggerFactory.getLogger(KafkaStormService.class);
	@Autowired
    @Qualifier("inputToKafka")
    MessageChannel channel;
	
	@Override
	public void processMessage(Map<Object, Map<Object, Object>> message) throws Exception{
		onMessage(message);
		
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void onMessage(Map<Object, Map<Object, Object>> message) throws Exception {
		System.out.println(gson.toJson(message));
		for (Map.Entry < Object,Map<Object, Object>>entry:  message.entrySet()){
            System.out.println("Suchit Topic:" + entry.getKey());
            for (Entry<Object, Object> msg : entry.getValue().entrySet()) {
            	List<String> list=(List<String>) msg.getValue();
            	System.out.println("key=========>"+msg.getKey());
            	for (String json : list) {
            		Map data=gson.fromJson(json, Map.class);
            		String equipment=data.get("equipment").toString();
            		String key=(String) data.get("key");
            		
	            	if (!pressuresMap.containsKey(equipment.trim())) {
	            		pressuresMap.put(equipment.trim(), new ArrayList<Map>());
					}
            		if (pressuresMap.get(equipment.trim()).size()==10) {
            			List<Map> pressuresCopy =new ArrayList<Map>();
            			pressuresCopy.addAll(pressuresMap.get(equipment.trim()));
            			pressuresMap.put(equipment.trim(), new ArrayList<Map>());
            			String pressuresJson=gson.toJson(pressuresCopy);
            			send(pressuresJson);
            		}
            		
            		if (key.trim().equals("pressure")) {
            			pressuresMap.get(equipment.trim()).add(data);
            		}
            		
				}
            	
            	
            }
        }
	}
	
	private boolean send(String data) throws Exception{
		logger.info(data);
		Message<String> msg = MessageBuilder.withPayload(data)
				.setHeader(KafkaHeaders.TOPIC, "IOT_DATA_STORM").build();
		return channel.send(msg);
	
	}
}
