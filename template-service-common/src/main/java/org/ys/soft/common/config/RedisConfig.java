package org.ys.soft.common.config;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import redis.clients.jedis.JedisPoolConfig;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Value("${spring.redis.host}")
	private String host;

	@Value("${spring.redis.port}")
	private int port;

	@Value("${spring.redis.timeout}")
	private int timeout;

	@Value("${spring.redis.password}")
	private String password;

	@Value("${spring.redis.database}")
	private int database;

	@Value("${spring.redis.pool.max-idle}")
	private int maxIdle;

	@Value("${spring.redis.pool.min-idle}")
	private int minIdle;

	@Value("${spring.redis.pool.max-active}")
	private int maxActive;

	@Value("${spring.redis.pool.max-wait}")
	private int maxWait;

	/**
	 * [注解@Cache key生成规则]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	@Bean
	public KeyGenerator keyGenerator() {
		return new KeyGenerator() {
			@Override
			public Object generate(Object target, Method method, Object... params) {
				StringBuilder sb = new StringBuilder();
				sb.append(target.getClass().getName());
				sb.append(method.getName());
				for (Object obj : params) {
					sb.append(obj.toString());
				}
				return sb.toString();
			}
		};
	}

	/**
	 * [注解@Cache的管理器,设置过期时间的单位是秒]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	@Bean
	public CacheManager cacheManager(RedisTemplate<String, String> redisTemplate) {
		RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
		return cacheManager;
	}

	/**
	 * [redis模板,存储关键字是字符串，值是Jdk序列化]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Bean
	public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {
		StringRedisTemplate redisTemplate = new StringRedisTemplate(factory);
		Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
		ObjectMapper om = new ObjectMapper();
		om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		jackson2JsonRedisSerializer.setObjectMapper(om);
		redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
		redisTemplate.afterPropertiesSet();
		return redisTemplate;
	}

	/**
	 * [redis连接的基础设置]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	@Bean
	public JedisConnectionFactory redisConnectionFactory() {
		JedisConnectionFactory factory = new JedisConnectionFactory();
		factory.setHostName(host);
		factory.setPort(port);
		factory.setPassword(password);
		factory.setDatabase(database);
		factory.setTimeout(timeout);
		factory.setUsePool(true);
		factory.setPoolConfig(jedisPoolConfig());
		return factory;
	}

	/**
	 * [连接池配置]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	@Bean
	public JedisPoolConfig jedisPoolConfig() {
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxIdle(maxIdle);
		jedisPoolConfig.setMinIdle(minIdle);
		jedisPoolConfig.setMaxTotal(maxActive);
		jedisPoolConfig.setMaxWaitMillis(maxWait);
		return jedisPoolConfig;
	}

	/**
	 * [异常处理]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	@Bean
	@Override
	public CacheErrorHandler errorHandler() {
		CacheErrorHandler cacheErrorHandler = new CacheErrorHandler() {
			@Override
			public void handleCacheGetError(RuntimeException e, Cache cache, Object key) {
				logger.error("redis异常：key=[{}]", key, e);
			}

			@Override
			public void handleCachePutError(RuntimeException e, Cache cache, Object key, Object value) {
				logger.error("redis异常：key=[{}]", key, e);
			}

			@Override
			public void handleCacheEvictError(RuntimeException e, Cache cache, Object key) {
				logger.error("redis异常：key=[{}]", key, e);
			}

			@Override
			public void handleCacheClearError(RuntimeException e, Cache cache) {
				logger.error("redis异常：", e);
			}
		};
		return cacheErrorHandler;
	}
}
