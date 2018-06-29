package org.ys.soft.framework.base.exception;

/**
 * [基础框架中产生的异常]
 * 
 * @author Chris li[黎超]
 * @version [版本, 2017-04-12]
 * @see
 */
public class FrameworkException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public FrameworkException() {
		super();
	}

	public FrameworkException(String errorMsg) {
		super(errorMsg);
	}

	public FrameworkException(String errorMsg, Throwable t) {
		super(errorMsg, t);
	}

}
