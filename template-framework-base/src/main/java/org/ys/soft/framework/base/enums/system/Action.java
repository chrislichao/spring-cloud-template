package org.ys.soft.framework.base.enums.system;

/**
 * [系统操作枚举类]
 * 
 * @author Chris li[黎超]
 * @version [版本, 2017-04-12]
 * @see Menu
 * @see Module
 */
public enum Action {

	/**
	 * [可使用]
	 */
	ACCESS("access", "可使用"),

	/**
	 * [访问主页]
	 */
	VISIT("visit", "访问主页"),

	/**
	 * [查询数据]
	 */
	QUERY("query", "查询数据"),

	/**
	 * [新增]
	 */
	ADD("add", "新增"),

	/**
	 * [修改]
	 */
	EDIT("edit", "修改"),

	/**
	 * [删除]
	 */
	DELETE("delete", "删除"),

	/**
	 * [查看]
	 */
	VIEW("view", "查看"),

	/**
	 * [配置]
	 */
	CONFIG("config", "配置");

	private final String code;

	private final String name;

	Action(String code, String name) {
		this.code = code;
		this.name = name;
	}

	/**
	 * [获取编码]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public String getCode() {
		return code;
	}

	/**
	 * [获取名称]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public String getName() {
		return name;
	}

}
