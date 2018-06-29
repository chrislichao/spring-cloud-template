package org.ys.soft.common.lock.distributed;

import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ys.soft.common.lock.Callback;
import org.ys.soft.common.lock.DistributedLock;
import org.ys.soft.framework.base.exception.business.UnableApplyLockException;

/**
 * [Redis实现的分布式锁]
 * 
 * @author Chris li[黎超]
 * @version [版本, 2017-04-12]
 * @see
 */
@Component
public class RedisLock implements DistributedLock {
	private static final String LOCK_PREFIX_FORMAT = "REDIS_LOCK_%s";

	@Autowired
	private RedissonClient client;

	@Override
	public <T> T lock(String lockName, Callback callback) {
		return lock(lockName, 100, callback);
	}

	@Override
	public <T> T lock(String lockName, long lockTime, Callback callback) {
		RLock lock = client.getLock(String.format(LOCK_PREFIX_FORMAT, lockName));
		try {
			boolean success = lock.tryLock(100, lockTime, TimeUnit.SECONDS);
			if (success) {
				try {
					return callback.execute();
				} finally {
					lock.unlock();
				}
			}
			throw new UnableApplyLockException("获取分布式锁失败!");
		} catch (Exception e) {
			throw new UnableApplyLockException("获取分布式锁失败!");
		}
	}
}
