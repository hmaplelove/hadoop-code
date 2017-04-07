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
@SuppressWarnings("rawtypes")
public class KafkaStormService implements KafkaService{
	private static final Logger logger = LoggerFactory.getLogger(KafkaStormService.class);
	private static Gson gson=new Gson();

	private static Map<String, List<Map>> maxupMap = new HashMap<String, List<Map>>();
	private static Map<String, List<Map>> maxdownMap = new HashMap<String, List<Map>>();
	@Autowired
    @Qualifier("inputToKafka")
    MessageChannel channel;
	
	@Override
	public void processMessage(Map<Object, Map<Object, Object>> message) throws Exception{
		onMessage(message);
	}
	
	@SuppressWarnings({ "unchecked" })
	private void onMessage(Map<Object, Map<Object, Object>> message) throws Exception {
		for (Map.Entry < Object,Map<Object, Object>>entry:  message.entrySet()){
            for (Entry<Object, Object> msg : entry.getValue().entrySet()) {
            	List<String> list=(List<String>) msg.getValue();
            	for (String json : list) {
            		logger.info("msg=========>"+json);
            		Map data=gson.fromJson(json, Map.class);
            		String equipment=data.get("equipment").toString();
            		String key=(String) data.get("k");
            		
            		//最大拔出力
            		if (key.trim().equals("maxup")) {
            			if (!maxupMap.containsKey(equipment.trim())) {
            				maxupMap.put(equipment.trim(), new ArrayList<Map>());
            			}
            			if (maxupMap.get(equipment.trim()).size()==50) {
            				List<Map> maxupCopy =new ArrayList<Map>();
            				maxupCopy.addAll(maxupMap.get(equipment.trim()));
            				maxupMap.put(equipment.trim(), new ArrayList<Map>());
            				String maxupJson=gson.toJson(maxupCopy);
            				send(maxupJson,key);
            			}
            			maxupMap.get(equipment.trim()).add(data);
            		}
            		
            		//最大插入力
            		if (key.trim().equals("maxdown")) {
            			if (!maxdownMap.containsKey(equipment.trim())) {
            				maxdownMap.put(equipment.trim(), new ArrayList<Map>());
            			}
            			if (maxupMap.get(equipment.trim()).size()==50) {
            				List<Map> maxdownCopy =new ArrayList<Map>();
            				maxdownCopy.addAll(maxdownMap.get(equipment.trim()));
            				maxdownMap.put(equipment.trim(), new ArrayList<Map>());
            				String maxdownJson=gson.toJson(maxdownCopy);
            				send(maxdownJson,key);
            			}
            			maxdownMap.get(equipment.trim()).add(data);
            		}
            		
				}
            }
        }
	}
	
	private boolean send(String data ,String key) throws Exception{
		logger.info("send to ["+key +"]=========>"+data);
		Message<String> msg = MessageBuilder.withPayload(data)
				.setHeader(KafkaHeaders.TOPIC, "IOT_DATA_"+key.toUpperCase()).build();
		return channel.send(msg);
	
	}
}
