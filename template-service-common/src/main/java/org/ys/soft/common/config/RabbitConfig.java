package org.ys.soft.common.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

	@Value("${system.log.queue}")
	private String systemLogQueue;

	@Bean
	public Queue systemLogQueue() {
		return new Queue(systemLogQueue);
	}
}
