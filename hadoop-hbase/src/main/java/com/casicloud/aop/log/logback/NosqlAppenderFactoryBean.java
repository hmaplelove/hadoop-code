package com.casicloud.aop.log.logback;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.util.Assert;

import com.casicloud.aop.log.logback.hbase.HbaseAppender;

/**
 * NosqlAppenderFactoryBean工厂类，根据Nosql类型返回实例
 */
@SuppressWarnings("rawtypes")
public class NosqlAppenderFactoryBean implements FactoryBean<NoSqlAppender> {

	private Object template;
	private String tbname;
	private String appenderPath;

	/**
	 * Appender类路径，实例化不同类型Appender实例
	 * @param appender
	 */
	public void setAppenderPath(String appender) {
		this.appenderPath = appender;
	}

	/**
	 * 指定类型数据库操作类
	 * @param template
	 */
	public void setTemplate(Object template) {
		this.template = template;
	}

	/**
	 * 数据存储表名
	 * @param tbname
	 */
	public void setTbname(String tbname) {
		this.tbname = tbname;
	}

	/**
	 * 根据数据库类型返回Appender实例
	 *
	 * @return
	 * @throws Exception
	 */
	@Override
	public NoSqlAppender getObject() throws Exception {
		//校验配置
		Assert.notNull(template);
		Assert.notNull(appenderPath);
		Assert.notNull(tbname);

		//生成实例
		Class<?> appenderClass = Class.forName(appenderPath);
		String SCname = appenderClass.getSuperclass().getSimpleName();
		if (SCname.equals("NoSqlAppender")) {
			NoSqlAppender hbaseAppender = (NoSqlAppender) appenderClass.newInstance();
			hbaseAppender.setTemplate(template);
			hbaseAppender.setTbname(tbname);
			return hbaseAppender;
		} else {
			throw new IllegalArgumentException(appenderPath + "is not NoSqlAppender subclass!");
		}
	}

	@Override
	public Class<?> getObjectType() {
		return HbaseAppender.class;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}
}