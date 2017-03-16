package com.casicloud.aop.log.logback.hbase;

import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.data.hadoop.hbase.TableCallback;
import org.springframework.stereotype.Repository;

import com.casicloud.aop.log.logback.ILogRepository;

/**
 * Hbase 日志存储实现类
 */

@SuppressWarnings("deprecation")
@Repository
public class HbaseLogRepository implements ILogRepository<HbaseTemplate> {

	private String tbname;

	private HbaseTemplate hbaseTemplate;

	//日志列族
	public static byte[] CF_INFO = Bytes.toBytes("log");

	//日志列名
	private byte[] CF_CELL = Bytes.toBytes("data");

	//日志表名
	public void setTbname(String tbname) {
		this.tbname = tbname;
	}

	public void setHbaseTemplate(HbaseTemplate hbaseTemplate) {
		this.hbaseTemplate = hbaseTemplate;
	}
	
	/**
	 * 日志数据存储
	 *
	 * @param key
	 * @param value
	 */
	public void saveLog(String key, String value) {
		final byte[] bKey = Bytes.toBytes(key);
		final byte[] bValue = Bytes.toBytes(value);

		hbaseTemplate.execute(tbname, new TableCallback<Object>() {

			public Object doInTable(HTableInterface table) throws Throwable {
				Put p = new Put(bKey);
				p.add(CF_INFO, CF_CELL, bValue);
				table.put(p);
				return null;
			}
			
		});
	}

}