package com.casicloud.aop.log.logback.hbase;

import java.util.Random;

import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.util.Assert;

import com.casicloud.aop.log.logback.NoSqlAppender;

import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * Hbase Appender 实现类
 */
public class HbaseAppender extends NoSqlAppender<ILoggingEvent> implements InitializingBean {

	Random generator = new Random();

	private HBaseAdmin admin;

	/**
	 * 初始化，HbaseLogRepository
	 */
	@Override
	public void start() {
		Assert.notNull(template, "hbaseTemplate not null !");
		try {
			afterPropertiesSet();
		} catch (Exception e) {
			e.printStackTrace();
		}
		HbaseLogRepository repository = new HbaseLogRepository();
		repository.setHbaseTemplate((HbaseTemplate) template);
		repository.setTbname(tbname);
		logRepository = repository;
		super.start();
	}

	/**
	 * 生成记录KEY，如果有必要也可以通过patternLayout生成
	 *
	 * @param event
	 * @return
	 */
	@Override
	protected String generatedKey(ILoggingEvent event) {
		//使用随机数防止并发生成同名KEY
		int id = generator.nextInt(9999) + 1000;

		StringBuilder sb = new StringBuilder();
		sb.append(event.getLoggerName());
		sb.append(event.getLevel());
		sb.append(event.getThreadName());
		sb.append(event.getTimeStamp());
		sb.append("-");
		sb.append(id);
		return sb.toString().toLowerCase().replaceAll(" ", "");
	}

	/**
	 * 没有表自动创建
	 *
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void afterPropertiesSet() throws Exception {
		admin = new HBaseAdmin(((HbaseTemplate) template).getConfiguration());
		if (!admin.tableExists(tbname)) {
			HTableDescriptor tableDescriptor = new HTableDescriptor(tbname);
			HColumnDescriptor columnDescriptor = new HColumnDescriptor(HbaseLogRepository.CF_INFO);
			tableDescriptor.addFamily(columnDescriptor);
			admin.createTable(tableDescriptor);
		}
	}

	

}
