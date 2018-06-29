package org.ys.soft.framework.base.enums.system;

import org.ys.soft.framework.base.BaseConstants;

/**
 * [系统模块枚举类]
 * 
 * @author Chris li[黎超]
 * @version [版本, 2017-04-12]
 * @see Action
 * @see Menu
 */
public enum Module {
	/**
	 * [空模块]
	 */
	NULL(BaseConstants.NULL, BaseConstants.NULL),
	/**
	 * [个人中心]
	 */
	PERSON("person", "个人中心"),
	/**
	 * [日志查询]
	 */
	LOG("log", "日志查询"),
	/**
	 * [系统管理]
	 */
	SYSTEM("system", "系统管理");

	private final String code;

	private final String name;

	Module(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

}
