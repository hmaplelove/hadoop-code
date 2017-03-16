package com.casicloud.aop.log.logback;

import org.springframework.util.Assert;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;

/**
 * NoSql Appender 基础类
 * 子类需要实现generatedKey方法和指定存储类实例
 */
abstract public class NoSqlAppender<E> extends UnsynchronizedAppenderBase<E> {

	//日志存储
	@SuppressWarnings("rawtypes")
	protected ILogRepository logRepository;
	//日志表名
	protected String tbname;
	//日志存储格式
	protected String pattern;
	//日志格式解析器
	protected PatternLayout patternLayout;
	//Nosql操作类
	protected Object template;

	/**
	 * 日志存储KEY生成
	 *
	 * @param event
	 * @return
	 */
	protected abstract String generatedKey(E event);

	/**
	 * 使用指定的格式生成日志内容数据
	 *
	 * @param event
	 * @return
	 */
	protected String generatedValue(E event) {
		return patternLayout.doLayout((ILoggingEvent) event);
	}

	/**
	 * 日志表名
	 *
	 * @param tbname
	 */
	public void setTbname(String tbname) {
		this.tbname = tbname;
	}

	/**
	 * 日志存储
	 *
	 * @param eventObject
	 */
	@Override
	protected void append(E eventObject) {
		if (!isStarted()) {
			return;
		}
		try {
			String key = generatedKey(eventObject);
			String value = generatedValue(eventObject);
			logRepository.saveLog(key, value);
		} catch (Exception e) {
			addError(e.getMessage());
		}
	}

	/**
	 * 初始化，patternLayout
	 */
	@Override
	public void start() {
		Assert.notNull(tbname, "tbname not null !");

		patternLayout = new PatternLayout();
		patternLayout.setPattern(pattern);
		patternLayout.setContext(context);
		patternLayout.setOutputPatternAsHeader(false);
		patternLayout.start();
		super.start();
	}

	@Override
	public void stop() {
		super.stop();
	}

	/**
	 * 日志格式
	 *
	 * @param pattern
	 */
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public void setTemplate(Object template) {
		this.template = template;
	}

}