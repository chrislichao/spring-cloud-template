package org.ys.soft.framework.base;

import org.ys.soft.framework.base.enums.database.DatabaseCaseType;

/**
 * [系统常量]
 * 
 * @author Chris li[黎超]
 * @version [版本, 2017-04-12]
 * @see
 */
public class BaseConstants {
	/**
	 * 系统自动生成数据库表别名前缀
	 */
	public static final String DB_TABLE_ALIAS_PREFIX = "cl_";
	/**
	 * 当前登录用户存于session中的key
	 */
	public static final String SESSION_KEY_USER = "currentUser";
	/**
	 * 超级管理员账号id
	 */
	public static final Long SUPERADMIN_ID = -1L;
	/**
	 * 超级管理员密码
	 */
	public static final String DEFAULT_USER_PWD = "123456";
	/**
	 * 系统名称(key)
	 */
	public static final String SYSTEM_NAME_KEY = "systemName";
	/**
	 * 上下文路径(key)
	 */
	public static final String CONTEXT_PATH_KEY = "path";
	/**
	 * 空
	 */
	public static final String NULL = "null";
	/**
	 * 系统中配置的数据库大小写策略
	 */
	public static DatabaseCaseType APPLICATION_DB_CASE_TYPE = DatabaseCaseType.UPPERCASE;

}
