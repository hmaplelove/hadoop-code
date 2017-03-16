package com.casicloud.aop.log.logback;

/**
 * 日志存储接口
 */
public interface ILogRepository<E> {

    /**
     * 保存日志，KEY-VALUE形式
     *
     * @param key
     * @param value
     */
    public void saveLog(String key, String value);

}
