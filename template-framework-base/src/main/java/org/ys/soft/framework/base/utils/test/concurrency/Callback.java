package org.ys.soft.framework.base.utils.test.concurrency;

/**
 * [并发的业务回调接口]
 * 
 * @author Chris li[黎超]
 * @version [版本, 2017-04-12]
 * @see
 */
public interface Callback {
	/**
	 * [需经过并发测试的业务逻辑]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public void execute();
}
