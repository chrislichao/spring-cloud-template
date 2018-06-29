package org.ys.soft.framework.base.enums;

/**
 * [Controller返回结果的结果码]
 * 
 * @author Chris li[黎超]
 * @version [版本, 2017-04-12]
 * @see
 */
public enum ErrorCode {
	/**
	 * 成功
	 */
	SUCCESS("0000"),
	/**
	 * 用户未登陆
	 */
	NOT_LOGIN("1000"),
	/**
	 * 用户无权限
	 */
	UNAUTHORIZED("1001"),
	/**
	 * 失败
	 */
	ERROR("9999"),

	;

	private final String code;

	ErrorCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}
