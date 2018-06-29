package org.ys.soft.framework.base.enums.database;

/**
 * [左连接分组策略,目前仅供LeftJoin注解使用]
 * 
 * @author Chris li[黎超]
 * @version [版本, 2017-04-12]
 * @see LeftJoin
 */
public enum Group {
	ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9), TEN(10);

	private final Integer value;

	Group(Integer value) {
		this.value = value;
	}

	public Integer getValue() {
		return this.value;
	}

	@Override
	public String toString() {
		return getValue().toString();
	}
}
