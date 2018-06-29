package org.chris.li;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.ApplicationContext;

@EnableEurekaServer
@SpringBootApplication
public class ServerApplication {
	private static Logger logger = LoggerFactory.getLogger(ServerApplication.class);

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(ServerApplication.class, args);
		String[] activeProfiles = context.getEnvironment().getActiveProfiles();
		for (String profile : activeProfiles) {
			logger.info("====================================");
			logger.info("Server started with profile [ {} ]", profile);
			logger.info("====================================");
		}
	}
}
