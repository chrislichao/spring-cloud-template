package org.ys.soft.common.lock;

/**
 * [获取到分布式锁后执行的业务回调接口]
 * 
 * @author Chris li[黎超]
 * @version [版本, 2017-04-12]
 * @see
 */
public interface Callback {
	/**
	 * [获取到分布式锁后执行的业务逻辑]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public <T> T execute();
}
