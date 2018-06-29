package org.ys.soft.framework.base.utils.database;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.ys.soft.framework.base.BaseConstants;
import org.ys.soft.framework.base.annotation.database.Column;
import org.ys.soft.framework.base.annotation.database.Id;
import org.ys.soft.framework.base.annotation.database.LeftJoin;
import org.ys.soft.framework.base.annotation.database.Table;
import org.ys.soft.framework.base.enums.database.DatabaseCaseType;
import org.ys.soft.framework.base.exception.FrameworkException;
import org.ys.soft.framework.base.model.BaseEntity;
import org.ys.soft.framework.base.utils.Assert;
import org.ys.soft.framework.base.utils.reflect.ReflectUtil;

/**
 * [数据库相关的工具类]
 * 
 * @author Chris li[黎超]
 * @version [版本, 2017-04-12]
 * @see
 */
public class DatabaseUtil {
	/**
	 * [SQL注入的正则表达式]
	 */
	private static String SQL_INJECT_REGEX = "(?:')|(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|"
			+ "(\\b(select|update|union|and|or|delete|insert|trancate|char|into|substr|ascii|declare|exec|count|master|into|drop|execute)\\b)";

	/**
	 * [验证参数是否存在SQL注入嫌疑]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static void validateBefore(Object value) {
		Assert.notNull(value, "参数不允许为空!");
		Assert.notBlank(value.toString(), "参数不允许为空白字符串!");

		Pattern pattern = Pattern.compile(SQL_INJECT_REGEX);
		Matcher matcher = pattern.matcher(value.toString());
		StringBuffer sensitiveBuffer = new StringBuffer();
		while (matcher.find()) {
			sensitiveBuffer.append("{").append(matcher.group()).append("}");
		}

		Assert.isBlank(sensitiveBuffer.toString(), "参数中包含敏感字符串:" + sensitiveBuffer.toString() + ",请修改后重试!");
	}

	/**
	 * [根据用户设置DataBase大小写策略更新表名和字段名]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static String switchByDatabaseCaseType(String oldStr) {
		return BaseConstants.APPLICATION_DB_CASE_TYPE.equals(DatabaseCaseType.UPPERCASE) ? oldStr.toUpperCase() : oldStr.toLowerCase();
	}

	/**
	 * [根据用户设置DataBase大小写策略获取表的别名]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static String getTableAlias(String tableName) {
		return switchByDatabaseCaseType(BaseConstants.DB_TABLE_ALIAS_PREFIX + tableName);
	}

	/**
	 * [按照驼峰-下划线格式,将类名转换成数据库表名,或者将字段转换成列名]<br>
	 * eg:MyTable --> MY_TABLE<br>
	 * eg:createTime --> CREATE_TIME
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static String switchForDatebase(String oldName) {
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append(oldName.charAt(0));
		// 从第二个字符开始,检查字符如果是大写的,则在前面补下划线
		for (int i = 1; i < oldName.length(); i++) {
			if (Character.isUpperCase(oldName.charAt(i))) {
				sbuffer.append("_");
			}
			sbuffer.append(oldName.charAt(i));
		}
		// 返回时变大写
		return switchByDatabaseCaseType(sbuffer.toString());
	}

	/**
	 * [对象转换为String,数据库字段的值 TODO 待完善]<br>
	 * eg: "abc" --> "'abc'"<br>
	 * eg: 9 --> "9"
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static String switchObjectValueForDatabase(Object obj) {
		if (obj instanceof String) {
			return "'" + obj.toString() + "'";
		}
		if (obj instanceof Date) {
			return "sysdate";
		}
		return obj.toString();
	}

	/**
	 * 
	 * 
	 * @return
	 */
	/**
	 * [用逗号分隔的字符串组用单引号环绕]<br>
	 * eg:chris,li,qq,love --> 'chris','li','qq','love'
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static String wrapSingleQuotes(String strs) {
		StringBuffer buffer = new StringBuffer();
		for (String str : strs.split(",")) {
			buffer.append(",'").append(str).append("'");
		}
		return buffer.toString().equals("") ? buffer.toString() : buffer.toString().substring(1);
	}

	/**
	 * [获取关联表的表名]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static String getReferenceTableName(Class<?> clazz) {
		Assert.isAnnotationPresent(Table.class, clazz, String.format("Class[%s] is not presented by annotation[%s].", clazz.getName(), Table.class
				.getName()));
		// 设置Table表名
		Table table = clazz.getAnnotation(Table.class);
		String tableName = table.name();
		if (StringUtils.isBlank(tableName)) {
			tableName = switchForDatebase(clazz.getSimpleName());
		}
		return switchByDatabaseCaseType(tableName);
	}

	/**
	 * [获取关联表的别名]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static String getReferenceTableAlias(LeftJoin leftJoin) {
		return getTableAlias(getReferenceTableName(leftJoin.refModel())) + "_" + leftJoin.group();
	}

	/**
	 * [获取关联表中取值字段的列名]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static String getReferenceValueFieldColumn(LeftJoin leftJoin) {
		Field valField = ReflectUtil.getField(leftJoin.refModel(), BaseEntity.class, leftJoin.refValField());
		return getColumnName(valField, leftJoin.refValField(), leftJoin.refModel().getName());
	}

	/**
	 * [获取本表中的on列名]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static String getSelfOnFieldColumn(Class<?> selfClazz, LeftJoin leftJoin){
		Field onField = ReflectUtil.getField(selfClazz, BaseEntity.class, leftJoin.selfOnField());
		return getColumnName(onField, leftJoin.selfOnField(), leftJoin.refModel().getName());
	}

	/**
	 * [获取关联表中的on列名]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static String getReferenceOnFieldColumn(LeftJoin leftJoin){
		Field onField = ReflectUtil.getField(leftJoin.refModel(), BaseEntity.class, leftJoin.refOnField());
		return getColumnName(onField, leftJoin.refOnField(), leftJoin.refModel().getName());
	}

	/**
	 * [获取对应的列名]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	private static String getColumnName(Field valField, String refModelClass, String refValFieldName) {
		if (valField.isAnnotationPresent(Id.class)) {
			Id id = valField.getAnnotation(Id.class);
			String columnName = switchForDatebase(valField.getName());
			if (StringUtils.isNotBlank(id.name())) {
				columnName = switchByDatabaseCaseType(id.name());
			}
			return columnName;
		}
		if (valField.isAnnotationPresent(Column.class)) {
			Column column = valField.getAnnotation(Column.class);
			// 默认表列名为字段名大写
			String columnName = switchForDatebase(valField.getName());
			if (StringUtils.isNotBlank(column.name())) {
				// 有值则直接取大写值
				columnName = switchByDatabaseCaseType(column.name());
			}
			return columnName;
		}
		// 如果对应的字段未被Id或Column注解标注,则抛异常
		String errorMsg = String.format("Error!Can not find column by ModelClass[%s] and Field[%s].", refModelClass, refValFieldName);
		throw new FrameworkException(errorMsg);
	}
}
