package com.casicloud.aop.log.logback;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.filter.LevelFilter;
import ch.qos.logback.core.spi.FilterReply;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.util.Map;


/**
 * Logback Appender与Spring整合类
 */
@SuppressWarnings({"unchecked","rawtypes"})
public class SpringLogbackBean implements InitializingBean {

	org.slf4j.Logger _logger = LoggerFactory.getLogger(this.getClass());

	private Level logLevel = Level.INFO;
	private String appenderName = "NoSqlAppender";
	private NoSqlAppender appender;
	private Object logName = "root";
	private Level filterLevel = Level.INFO;
	private boolean useFilterLevel = true;
	private boolean additiveAppender = true;
	private String pattern = "%d{HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n";

	/**
	 * Appender 名称
	 *
	 * @param appenderName
	 */
	public void setAppenderName(String appenderName) {
		this.appenderName = appenderName;
	}

	/**
	 * 设置存储的日志级别，默认是INFO
	 *
	 * @param logLevel
	 */
	public void setLogLevel(Level logLevel) {
		this.logLevel = logLevel;
	}

	/**
	 * 设置Logback appender
	 *
	 * @param appender
	 */
	public void setAppender(NoSqlAppender appender) {
		this.appender = appender;
	}

	/**
	 * 设置需要记录的日志名，默认是root
	 *
	 * @param logName
	 */
	public void setLogName(Object logName) {
		this.logName = logName;
	}

	/**
	 * 使用过滤器过滤的日志级别，默认INFO
	 *
	 * @param filterLevel
	 */
	public void setFilterLevel(Level filterLevel) {
		this.filterLevel = filterLevel;
	}

	/**
	 * 是否累加Appender(继承root appender)，默认 true
	 *
	 * @param additiveAppender
	 */
	public void setAdditiveAppender(boolean additiveAppender) {
		this.additiveAppender = additiveAppender;
	}

	/**
	 * 是否使用日志过滤，其它级别日志数据交给继承来的APPENDER处理
	 *
	 * @param useFilterLevel
	 */
	public void setUseFilterLevel(boolean useFilterLevel) {
		this.useFilterLevel = useFilterLevel;
	}

	/**
	 * 设置日志格式
	 *
	 * @param pattern
	 */
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(appender, "property `appender` must set!");
		buildAppender();
		//多LOGGER支持配置
		if (logName.getClass().getSimpleName().equals("LinkedHashMap")) {
			Map<String, String> loggers = (Map) logName;
			for (Map.Entry<String, String> log : loggers.entrySet()) {
				Logger logger = (Logger) LoggerFactory.getLogger(log.getKey());
				logger.setLevel(Level.toLevel(log.getValue()));
				logger.setAdditive(additiveAppender);
				logger.addAppender(appender);
				_logger.debug("Set appender: {} to logger: {} ", appender.getName(), log.getKey());
			}
		}
		//单个配置
		else {
			Logger logger = (Logger) LoggerFactory.getLogger(logName.toString());
			//将appender添加到指定logger
			logger.setLevel(logLevel);
			logger.setAdditive(additiveAppender);
			logger.addAppender(appender);
			_logger.debug("Set appender: {} to logger: {} ", appender.getName(), logName);
		}

	}

	
	private void buildAppender() {
		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
		//启用级别过滤，适用场景：把级别为 warn 是放入数据库。
		if (useFilterLevel) {
			LevelFilter levelFilter = new LevelFilter();
			levelFilter.setContext(loggerContext);
			levelFilter.setLevel(filterLevel);
			levelFilter.setOnMatch(FilterReply.ACCEPT);
			levelFilter.setOnMismatch(FilterReply.DENY);
			levelFilter.start();
			appender.addFilter(levelFilter);
		}
		//设置appender相关属性
		appender.setName(appenderName);
		appender.setPattern(pattern);
		appender.setContext(loggerContext);
		appender.start();
	}
}
