package org.ys.soft.common.service;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * [系统监控日志服务类]
 * 
 * @author Chris li[黎超]
 * @version [版本, 2017-04-12]
 * @see
 */
@Component
public class RedisCommonService {
	/**
	 * [定义锁]
	 */
	private final Lock lock = new ReentrantLock();
	/**
	 * [限流功能在redis中Key的固定值]
	 */
	private final String RATE_LIMIT_KEY_FORMAT = "RATE_LIMIT_SECONDS_%d";
	/**
	 * [默认的访问次数为1]
	 */
	private final long DEFAULT_VISIT_COUNT = 1;
	/**
	 * [定义最大并发访问数]
	 */
	private final long MAX_VISIT_COUNT = 99999;

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	/**
	 * [获取本时间周期{timeout}秒内,{visitUrl}链接的访问次数]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public long getVisitCount(long timeout, String visitUrl) {
		String rateLimitKey = String.format(RATE_LIMIT_KEY_FORMAT, timeout);
		// 当前线程获取到锁
		lock.lock();
		try {
			// 获取当前时间段访问数
			Object visitCountObj = redisTemplate.opsForHash().get(rateLimitKey, visitUrl);
			if (visitCountObj == null) {
				redisTemplate.opsForHash().putIfAbsent(rateLimitKey, visitUrl, String.valueOf(DEFAULT_VISIT_COUNT + 1));
				redisTemplate.expire(rateLimitKey, timeout, TimeUnit.SECONDS);
				return DEFAULT_VISIT_COUNT;
			}
			// 若访问数不为空,访问数自增1
			redisTemplate.opsForHash().increment(rateLimitKey, visitUrl, 1);
			return Long.valueOf(visitCountObj.toString());
		} catch (Exception e) {
			return MAX_VISIT_COUNT;
		} finally {
			lock.unlock();
		}
	}
}
