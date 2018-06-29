package org.ys.soft.framework.base.exception.business;

/**
 * [未申请到锁异常]
 * 
 * @author Chris li[黎超]
 * @version [版本, 2017-04-12]
 * @see
 */
public class UnableApplyLockException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public UnableApplyLockException(String errorMsg) {
		super(errorMsg);
	}

	public UnableApplyLockException(String errorMsg, Throwable t) {
		super(errorMsg, t);
	}

}
