package org.ys.soft.framework.base.exception.business;

/**
 * [系统中的业务异常]
 * 
 * @author Chris li[黎超]
 * @version [版本, 2017-04-12]
 * @see
 */
public class BusinessException extends Exception {
	private static final long serialVersionUID = 1L;

	public BusinessException(String errorMsg) {
		super(errorMsg);
	}

	public BusinessException(String errorMsg, Throwable t) {
		super(errorMsg, t);
	}

}
