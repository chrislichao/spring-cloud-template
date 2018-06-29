package org.ys.soft.framework.base.exception.business;

/**
 * [未登录异常,即用户未登录,或登录超时]
 * 
 * @author Chris li[黎超]
 * @version [版本, 2017-04-12]
 * @see
 */
public class NotLoggedException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public NotLoggedException() {
		super();
	}

	public NotLoggedException(String errorMsg) {
		super(errorMsg);
	}

	public NotLoggedException(String errorMsg, Throwable t) {
		super(errorMsg, t);
	}

}
