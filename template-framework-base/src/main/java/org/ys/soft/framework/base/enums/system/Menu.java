package org.ys.soft.framework.base.enums.system;

import org.ys.soft.framework.base.BaseConstants;

/**
 * [系统菜单枚举类]
 * 
 * @author Chris li[黎超]
 * @version [版本, 2017-04-12]
 * @see Action
 * @see Module
 */
public enum Menu {
	/**
	 * [空菜单,没有实际意义]
	 */
	NULL(Module.NULL, BaseConstants.NULL, BaseConstants.NULL, BaseConstants.NULL),
	/**
	 * [权限管理]
	 */
	PERMISSION_MANAGER(Module.SYSTEM, "permissionManager", "权限管理", "/sys/permission/main.htm"),
	/**
	 * [角色管理]
	 */
	ROLE_MANAGER(Module.SYSTEM, "roleManager", "角色管理", "/sys/role/main.htm"),
	/**
	 * [用户管理]
	 */
	USER_MANAGER(Module.SYSTEM, "userManager", "用户管理", "/sys/user/main.htm");

	private final Module module;

	private final String code;

	private final String name;

	private final String url;

	Menu(Module module, String code, String name, String url) {
		this.module = module;
		this.code = code;
		this.name = name;
		this.url = url;
	}

	public Module getModule() {
		return module;
	}

	public String getModuleCode() {
		return module.getCode();
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public String getUrl() {
		return url;
	}

	/**
	 * [通过传入的menuCode值获取对应的枚举类型]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static Menu getMatchedInstance(String menuCode) {
		// 循环比较menuCode值,匹配的值返回
		for (Menu menu : Menu.values()) {
			if (menu.getCode().equals(menuCode)) {
				return menu;
			}
		}
		// 默认返回NULL
		return Menu.NULL;
	}
}
