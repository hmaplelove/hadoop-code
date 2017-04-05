package com.casicloud.aop.kafka.core.service;

import java.io.IOException;
import java.util.Map;

public interface KafkaService {
	void processMessage(Map<Object, Map<Object, Object>> message) throws IOException;
}
