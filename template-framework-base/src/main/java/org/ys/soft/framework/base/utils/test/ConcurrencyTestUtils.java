package org.ys.soft.framework.base.utils.test;

import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ys.soft.framework.base.utils.test.concurrency.Callback;

/**
 * [并发测试的工具类]
 * 
 * @author Chris li[黎超]
 * @version [版本, 2017-04-12]
 * @see
 */
public class ConcurrencyTestUtils {

	private static Logger logger = LoggerFactory.getLogger(ConcurrencyTestUtils.class);

	private Callback callBack;

	/**
	 * 并发测试的工具类构造方法
	 */
	public ConcurrencyTestUtils(Callback callBack) {
		this.callBack = callBack;
	}

	/**
	 * [执行并发测试]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public void doTest(int concurrencyCount) {
		final CountDownLatch begin = new CountDownLatch(1);
		final CountDownLatch end = new CountDownLatch(concurrencyCount);
		for (int i = 0; i < concurrencyCount; i++) {
			new Thread() {
				@Override
				public void run() {
					try {
						logger.info(Thread.currentThread() + "准备就绪!");
						begin.await();
						callBack.execute();
					} catch (Exception e) {
						logger.error("并发出现异常!", e);
					} finally {
						end.countDown();
					}
				}
			}.start();
		}
		try {
			Thread.sleep(2000);
			logger.info("######################## 开始并发啦!");
			begin.countDown();
			end.await();
			logger.info("$$$$$$$$$$$$$$$$$$$$$$$$ 并发结束啦!");
		} catch (Exception e) {
			logger.error("并发出现异常!", e);
		}
	}
}
