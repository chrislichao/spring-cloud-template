package org.ys.soft.common.lock;

/**
 * [分布式锁接口]
 * 
 * @author Chris li[黎超]
 * @version [版本, 2017-04-12]
 * @see
 */
public interface DistributedLock {
	/**
	 * [获取锁{lockName},最多等待100秒.持有锁100秒后,自动释放锁,执行{callback}方法]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public <T> T lock(String lockName, Callback callback);
	/**
	 * [获取锁{lockName},最多等待100秒.持有锁{lockTime}秒后,自动释放锁,执行{callback}方法]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public <T> T lock(String lockName, long lockTime, Callback callback);
}
