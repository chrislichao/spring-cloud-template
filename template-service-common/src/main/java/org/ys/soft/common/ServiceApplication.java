package org.ys.soft.common;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * [{@code @SpringBootApplication}是SpringBoot项目的核心注解,主要用来开启自动配置. ]
 * 
 * @author Chris li[黎超]
 * @version [版本, 2017-04-12]
 * @see
 */
@SpringBootApplication
@ComponentScan(basePackages = "org.ys.soft")
@MapperScan("org.ys.soft.common.dao")
@EnableEurekaClient
public class ServiceApplication {

	/**
	 * [SpringBoot启动的方法]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static void main(String[] args) {
		SpringApplication.run(ServiceApplication.class, args);
	}
}
