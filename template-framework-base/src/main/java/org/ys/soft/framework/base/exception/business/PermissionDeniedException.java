package org.ys.soft.framework.base.exception.business;

/**
 * [权限不足异常,即用户没有足够权限]
 * 
 * @author Chris li[黎超]
 * @version [版本, 2017-04-12]
 * @see
 */
public class PermissionDeniedException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public PermissionDeniedException(String errorMsg) {
		super(errorMsg);
	}

	public PermissionDeniedException(String errorMsg, Throwable t) {
		super(errorMsg, t);
	}

}
