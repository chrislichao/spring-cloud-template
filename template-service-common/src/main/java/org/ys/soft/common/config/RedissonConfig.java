package org.ys.soft.common.config;

import org.apache.commons.lang.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Value("${spring.redisson.address}")
	private String address;

	@Value("${spring.redisson.timeout}")
	private int timeout;

	@Value("${spring.redisson.password}")
	private String password;

	@Value("${spring.redisson.connectionPoolSize}")
	private int connectionPoolSize;

	@Value("${spring.redisson.connectionMinimumIdleSize}")
	private int connectionMinimumIdleSize;

	@Bean
	RedissonClient redissonSingle() {
		Config config = new Config();
		SingleServerConfig serverConfig = config.useSingleServer().setAddress(address).setTimeout(timeout).setConnectionPoolSize(connectionPoolSize)
				.setConnectionMinimumIdleSize(connectionMinimumIdleSize);
		if (StringUtils.isNotBlank(password)) {
			serverConfig.setPassword(password);
		}
		logger.info("[Redisson] config init complate... address : " + address);
		return Redisson.create(config);
	}
}
