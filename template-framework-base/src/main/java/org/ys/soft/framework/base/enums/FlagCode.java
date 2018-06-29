package org.ys.soft.framework.base.enums;

/**
 * [开关码]
 * 
 * @author Chris li[黎超]
 * @version [版本, 2017-04-12]
 * @see
 */
public enum FlagCode {
	/** 是 */
	YES("1"),

	/** 否 */
	NO("0");

	private final String value;

	FlagCode(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}

}
