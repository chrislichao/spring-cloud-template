package org.ys.soft.framework.base.enums.database;

/**
 * [查询排序策略,目前仅供OrderBy注解使用]
 * 
 * @author Chris li[黎超]
 * @version [版本, 2017-04-12]
 * @see OrderBy
 */
public enum OrderByPolicy {
	/**
	 * 顺序排列
	 */
	ASC("ASC"),
	/**
	 * 逆序排列
	 */
	DESC("DESC");

	private final String policy;

	OrderByPolicy(String policy) {
		this.policy = policy;
	}

	@Override
	public String toString() {
		return this.policy;
	}
}
