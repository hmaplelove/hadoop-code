package com.casicloud.aop.kafka.core.service.impl;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.casicloud.aop.kafka.core.service.KafkaService;
import com.google.gson.Gson;

public class KafkaHdfsService implements KafkaService{

	/*private static SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
	private static Map<String, List<String>> pressuresMap = new HashMap<String, List<String>>();
	
	@Autowired 
	HDFSClinet HdfsClinet;*/
	private static final Logger logger = LoggerFactory.getLogger(KafkaStormService.class);
	private static Gson gson=new Gson();
	
	@Override
	public void processMessage(Map<Object, Map<Object, Object>> message) throws IOException{
		logger.info("HDFS...");
		onMessage(message);
		
	}
	private void onMessage(Map<Object, Map<Object, Object>> message) throws IOException {
		logger.info(gson.toJson(message));
		/*
		for (Map.Entry < Object,Map<Object, Object>>entry:  message.entrySet()){
            System.out.println("Suchit Topic:" + entry.getKey());
            for (Entry<Object, Object> msg : entry.getValue().entrySet()) {
            	List<String> list=(List<String>) msg.getValue();
            	System.out.println("key=========>"+msg.getKey());
            	for (String json : list) {
            		Map data=new Gson().fromJson(json, Map.class);
            		String equipment=data.get("equipment").toString();
            		String key=(String) data.get("key");
            		String collecttime=data.get("collecttime").toString();
            		
            		long date=Long.valueOf(collecttime);
	            	String time=sdf.format(date);
	            	String dir=new StringBuffer("/IOT_DATA").append("/").append(equipment).append("/").append(key).append("/").append(time).toString();
	            	boolean  flag=HdfsClinet.createDirectory(dir);
	            	System.out.println("createDirectory["+dir+"] result====>"+flag);
	            	String filePath=new StringBuffer(dir).append("/").append(collecttime).append(".json").toString();
	            	HdfsClinet.createFile(filePath, json);
	            	if (!pressuresMap.containsKey(equipment.trim())) {
	            		pressuresMap.put(equipment.trim(), new ArrayList<String>());
					}
            		if (pressuresMap.get(equipment.trim()).size()==50) {
            			List<String> pressuresCopy =new ArrayList<String>();
            			pressuresCopy.addAll(pressuresMap.get(equipment.trim()));
            			pressuresMap.put(equipment.trim(), new ArrayList<String>());
            			StringBuffer pressuresJsons=new StringBuffer();
            			for (String pressuresJson : pressuresCopy) {
            				pressuresJsons.append(pressuresJson).append("\r\n");
						}
            			String dir_pressures=new StringBuffer("/Pressures_Data").append("/").append(equipment).append("/").toString();
            			boolean  flag_pressures=HdfsClinet.createDirectory(dir_pressures);
    	            	System.out.println("createDirectory["+dir_pressures+"] result====>"+flag_pressures);
    	            	String filePath_pressures=new StringBuffer(dir_pressures).append("/").append(System.currentTimeMillis()).append(".json").toString();
    	            	HdfsClinet.createFile(filePath_pressures, pressuresJsons.toString());
            		}
            		
            		if (key.trim().equals("pressure")) {
            			pressuresMap.get(equipment.trim()).add(json);
            		}
            		
            		logger.info(json);
				}
            	
            	
            }
        }
	*/}

}
