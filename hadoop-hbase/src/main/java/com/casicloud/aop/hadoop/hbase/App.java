package com.casicloud.aop.hadoop.hbase;


import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.UUID;

import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.data.hadoop.hbase.RowMapper;
import org.springframework.data.hadoop.hbase.TableCallback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.casicloud.aop.log.logback.hbase.HbaseLogRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class App {
	@Autowired
	private HbaseTemplate  hbaseTemplate;
	@Autowired
	private HbaseLogRepository  hbaseLogRepository;
	
	
	@Test
	public void testQuery() throws Exception {
		//put();
		org.slf4j.Logger _logger = LoggerFactory.getLogger(this.getClass());
		_logger.info("org.slf4j.Logger");
		hbaseLogRepository.saveLog("applog","测试消息日志");
		//find(); 
		
		/*Configuration config=hbaseTemplate.getConfiguration();
		Connection connection = ConnectionFactory.createConnection(config);
		Admin admin = connection.getAdmin();
		TableName[] tableNames=admin.listTableNames();
		
		for (TableName tableName : tableNames) {
			System.out.println(tableName.getQualifierAsString());
		}*/
	}


	private void find() {
		hbaseTemplate.find("user", "username", new RowMapper<Object>() {
			public Object mapRow(Result result, int rowNum) throws Exception {
				NavigableMap<byte[], byte[]> map=result.getFamilyMap(Bytes.toBytes("username"));
				for (Entry<byte[], byte[]> entry : map.entrySet()) {
					System.out.println(new String(entry.getKey())+"====>"+new String(entry.getValue()));
				}
				return null;
			} 
			
		});
	}


	private void put() {
		hbaseTemplate.execute("user", new TableCallback<Map<String, Object>>() {
			@SuppressWarnings("deprecation")
			public Map<String, Object> doInTable(HTableInterface table) throws Throwable {
				Put put=new Put(Bytes.toBytes(UUID.randomUUID().toString()));
				put.add(Bytes.toBytes("username"), Bytes.toBytes("en_name"), Bytes.toBytes("zhangxiaoxin"));
				put.add(Bytes.toBytes("username"), Bytes.toBytes("cn_name"), Bytes.toBytes("张晓鑫"));
				table.put(put);
				return null;
			}
		});
	}
}
