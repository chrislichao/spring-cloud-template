package org.ys.soft.framework.base.enums.database;

/**
 * [数据库大小写类型]
 * 
 * @author Chris li[黎超]
 * @version [版本, 2017-04-12]
 * @see
 */
public enum DatabaseCaseType {

	/** 大写 */
	UPPERCASE(1, "uppercase"),

	/** 小写 */
	LOWERCASE(0, "lowercase");

	/**
	 * 值
	 */
	private final Integer value;
	/**
	 * 描述
	 */
	private final String desc;

	DatabaseCaseType(Integer value, String desc) {
		this.value = value;
		this.desc = desc;
	}

	public Integer getValue() {
		return value;
	}

	public String getDesc() {
		return desc;
	}

	/**
	 * 通过传入的value值获取对应的枚举类型
	 * 
	 * @param value
	 * @return
	 */
	public static DatabaseCaseType getMatchedInstance(String valueStr) {
		Integer value = Integer.valueOf(valueStr);
		// 循环比较value值,匹配的值返回
		for (DatabaseCaseType t : DatabaseCaseType.values()) {
			if (t.getValue().intValue() == value.intValue()) {
				return t;
			}
		}
		// 默认返回大写
		return DatabaseCaseType.UPPERCASE;
	}

}
