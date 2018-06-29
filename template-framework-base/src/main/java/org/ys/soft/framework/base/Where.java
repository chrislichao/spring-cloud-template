package org.ys.soft.framework.base;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.ys.soft.framework.base.annotation.database.Column;
import org.ys.soft.framework.base.annotation.database.Id;
import org.ys.soft.framework.base.annotation.database.LeftJoin;
import org.ys.soft.framework.base.exception.FrameworkException;
import org.ys.soft.framework.base.model.BaseEntity;
import org.ys.soft.framework.base.model.CoreColumn;
import org.ys.soft.framework.base.model.CoreTable;
import org.ys.soft.framework.base.utils.Assert;
import org.ys.soft.framework.base.utils.database.DatabaseUtil;
import org.ys.soft.framework.base.utils.reflect.ReflectUtil;

/**
 * [单表自定义查询条件]
 * 
 * @author Chris li[黎超]
 * @version [版本, 2017-04-12]
 * @see
 */
public class Where implements Serializable {

	/**
	 * [field方法错误异常]
	 */
	private static final String FIELD_ERROR_MSG = "Method[field] must be the first or after method[and,or].";
	/**
	 * [oprate方法错误异常]
	 */
	private static final String OPRATE_ERROR_MSG = "Method[equal,notEqual,contains,notContains,startsWith,notStartsWith,endsWith,notEndsWith,in,notIn,greaterThan,gtoet,lessThan,ltoet,isNull,isNotNull] must after method[field].";
	/**
	 * [connect方法错误异常]
	 */
	private static final String CONNECT_ERROR_MSG = "Method[and,or] must after method[equal,notEqual,contains,notContains,startsWith,notStartsWith,endsWith,notEndsWith,in,notIn,greaterThan,gtoet,lessThan,ltoet,isNull,isNotNull].";
	/**
	 * [orderBy方法错误异常]
	 */
	private static final String ORDER_BY_ERROR_MSG = "Method[orderByAsc,orderByDesc] must after method[orderByAsc,orderByDesc] or after method[equal,notEqual,contains,notContains,startsWith,notStartsWith,endsWith,notEndsWith,in,notIn,greaterThan,gtoet,lessThan,ltoet,isNull,isNotNull].";

	private static final long serialVersionUID = 1L;
	private boolean nextIsField = true;
	private boolean nextIsOprate = false;
	private boolean nextIsConnect = false;
	private boolean nextIsOrderBy = true;

	private Class<?> clazz;

	/**
	 * 当前字段,在执行field方法时赋值
	 */
	private String curField = "";
	/**
	 * 存放所有条件()
	 */
	private StringBuffer whereSb = new StringBuffer();
	/**
	 * 存放排序字段
	 */
	private StringBuffer orderBySb = new StringBuffer();

	/**
	 * 存放查询字段信息
	 */
	private Map<String, String> fieldMap = new HashMap<String, String>();

	/**
	 * 存放查询字段和值信息
	 */
	private Map<String, String> fieldValueMap = new HashMap<String, String>();

	/**
	 * 存放左连接字段信息
	 */
	private Map<String, LeftJoin> fieldLeftJoinMap = new HashMap<String, LeftJoin>();

	/**
	 * 存放操作字段集合
	 */
	private List<String> fieldList = new ArrayList<String>();

	/**
	 * 私有构造方法,不能通过new创建对象,对应的实体类
	 * 
	 * @param clazz
	 */
	private Where(Class<?> clazz) {
		this.clazz = clazz;
	}

	/**
	 * [获取实体类的class]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public Class<?> getEntityClass() {
		Assert.notNull(this.clazz);
		return this.clazz;
	}

	// ------------------------------------------------字段-----------------------------------------------//
	/**
	 * [设置字段]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public Where field(String field) {
		curField = field;
		fieldList.add(field);
		// 必须先设置字段
		if (nextIsField) {
			whereSb.append(" ").append(fieldToColumn(field));
			nextIsField = false;
			nextIsOprate = true;
			nextIsConnect = false;
			nextIsOrderBy = false;
			return this;
		}
		if (nextIsConnect) {
			// 如果没有加上连接符,默认添加and
			whereSb.append(" AND ").append(fieldToColumn(field));
			nextIsField = false;
			nextIsOprate = true;
			nextIsConnect = false;
			nextIsOrderBy = false;
			return this;
		}
		throw new FrameworkException(FIELD_ERROR_MSG);
	}

	/**
	 * [更新左连接字段信息集合]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	private void updateFieldLeftJoinMap(String field) {
		// 判断是否能取到对应的字段
		Field f = ReflectUtil.getField(clazz, BaseEntity.class, field);
		// 被Id注解标注
		if (f.isAnnotationPresent(Id.class)) {
			Id id = f.getAnnotation(Id.class);
			if (StringUtils.isNotBlank(id.name())) {
				// 有值则直接取大写值
				fieldMap.put(field, DatabaseUtil.switchByDatabaseCaseType(id.name()));
			} else {
				fieldMap.put(field, DatabaseUtil.switchForDatebase(field));
			}
			return;
		}
		// 被Column注解标注
		if (f.isAnnotationPresent(Column.class)) {
			Column column = f.getAnnotation(Column.class);
			if (StringUtils.isNotBlank(column.name())) {
				// 有值则直接取大写值
				fieldMap.put(field, DatabaseUtil.switchByDatabaseCaseType(column.name()));
			} else {
				fieldMap.put(field, DatabaseUtil.switchForDatebase(field));
			}
			return;
		}
		if (f.isAnnotationPresent(LeftJoin.class)) {
			fieldMap.put(field, "refModelField");// 随便设值,不会用到
			fieldLeftJoinMap.put(field, f.getAnnotation(LeftJoin.class));
		}
	}

	// 操作符
	// equal,notEqual,contains,notContains,startsWith,notStartsWith,endsWith,notEndsWith,in,notIn,greaterThan,gtoet,lessThan,ltoet,isNull,isNotNull//
	/**
	 * [等于XXX]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public Where equal(Object obj) {
		DatabaseUtil.validateBefore(obj);
		oprate("=", DatabaseUtil.switchObjectValueForDatabase(obj));
		return this;
	}

	/**
	 * [不等于XXX]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public Where notEqual(Object obj) {
		DatabaseUtil.validateBefore(obj);
		oprate("!=", DatabaseUtil.switchObjectValueForDatabase(obj));
		return this;
	}

	/**
	 * [包含XXX]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public Where contains(String str) {
		DatabaseUtil.validateBefore(str);
		oprate("LIKE", "'%" + str + "%'");
		return this;
	}

	/**
	 * [不包含XXX]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public Where notContains(String str) {
		DatabaseUtil.validateBefore(str);
		oprate("NOT LIKE", "'%" + str + "%'");
		return this;
	}

	/**
	 * [以XXX开始]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public Where startsWith(String str) {
		DatabaseUtil.validateBefore(str);
		oprate("LIKE", "'" + str + "%'");
		return this;
	}

	/**
	 * [不是以XXX开始]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public Where notStartsWith(String str) {
		DatabaseUtil.validateBefore(str);
		oprate("NOT LIKE", "'" + str + "%'");
		return this;
	}

	/**
	 * [以XXX结束]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public Where endsWith(String str) {
		DatabaseUtil.validateBefore(str);
		oprate("LIKE", "'%" + str + "'");
		return this;
	}

	/**
	 * [不是以XXX结束]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public Where notEndsWith(String str) {
		DatabaseUtil.validateBefore(str);
		oprate("NOT LIKE", "'%" + str + "'");
		return this;
	}

	/**
	 * [在XXX范围内]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public Where in(String str) {
		DatabaseUtil.validateBefore(str);
		oprate("IN", "(" + str + ")");
		return this;
	}

	/**
	 * [不在XXX范围内]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public Where notIn(String str) {
		DatabaseUtil.validateBefore(str);
		oprate("NOT IN", "(" + str + ")");
		return this;
	}

	/**
	 * [大于XXX]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public Where greaterThan(String str) {
		DatabaseUtil.validateBefore(str);
		oprate(">", str);
		return this;
	}

	/**
	 * [(greaterThanOrEqualTo)大于等于XXX]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public Where gtoet(String str) {
		DatabaseUtil.validateBefore(str);
		oprate(">=", str);
		return this;
	}

	/**
	 * [小于XXX]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public Where lessThan(String str) {
		DatabaseUtil.validateBefore(str);
		oprate("<", str);
		return this;
	}

	/**
	 * [(lessThanOrEqualTo)小于等于XXX]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public Where ltoet(String str) {
		DatabaseUtil.validateBefore(str);
		oprate("<=", str);
		return this;
	}

	/**
	 * [为空]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public Where isNull() {
		oprate("IS NULL", "");
		return this;
	}

	/**
	 * [不为空]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public Where isNotNull() {
		oprate("IS NOT NULL", "");
		return this;
	}

	/**
	 * [操作符方法]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	private void oprate(String oprate, String value) {
		if (nextIsOprate) {
			whereSb.append(" ").append(oprate).append(" ").append(value);
			fieldValueMap.put(curField, " " + oprate + " " + value);
			nextIsField = false;
			nextIsOprate = false;
			nextIsConnect = true;
			nextIsOrderBy = true;
			return;
		}
		throw new FrameworkException(OPRATE_ERROR_MSG);
	}

	// ------------------------------------------------连接符-----------------------------------------------//
	/**
	 * [连接符and]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public Where and() {
		connect("AND");
		return this;
	}

	/**
	 * [连接符or]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public Where or() {
		connect("OR");
		return this;
	}

	/**
	 * [连接符方法]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	private void connect(String connect) {
		if (nextIsConnect) {
			whereSb.append(" ").append(connect);
			nextIsField = true;
			nextIsOprate = false;
			nextIsConnect = false;
			nextIsOrderBy = false;
			return;
		}
		throw new FrameworkException(CONNECT_ERROR_MSG);
	}

	// ------------------------------------------------排序-----------------------------------------------//
	/**
	 * [顺序排列]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public Where orderByAsc(String field) {
		orderBy(field, "ASC");
		return this;
	}

	/**
	 * [逆序排列]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public Where orderByDesc(String field) {
		orderBy(field, "DESC");
		return this;
	}

	/**
	 * [排序方法]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	private void orderBy(String field, String orderPolicy) {
		if (nextIsOrderBy) {
			if (!getOrderBy().equals("")) {
				orderBySb.append(",");
			}
			orderBySb.append(" ").append(field).append(" ").append(orderPolicy);
			nextIsField = false;
			nextIsOprate = false;
			nextIsConnect = false;
			nextIsOrderBy = true;
			return;
		}
		throw new FrameworkException(ORDER_BY_ERROR_MSG);
	}

	/**
	 * [实体类字段转换成数据库表字段]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	private String fieldToColumn(String field) {
		// 判断是否能取到对应的字段
		Field f = ReflectUtil.getField(clazz, BaseEntity.class, field);
		Assert.notNull(f);
		// 被Id注解标注
		if (f.isAnnotationPresent(Id.class)) {
			Id id = f.getAnnotation(Id.class);
			if (StringUtils.isNotBlank(id.name())) {
				return DatabaseUtil.switchByDatabaseCaseType(id.name());
			}
		}
		// 被Column注解标注
		if (f.isAnnotationPresent(Column.class)) {
			Column column = f.getAnnotation(Column.class);
			if (StringUtils.isNotBlank(column.name())) {
				// 有值则直接取大写值
				return DatabaseUtil.switchByDatabaseCaseType(column.name());
			}
		}
		// 否则返回约定的格式
		return DatabaseUtil.switchForDatebase(field);
	}

	@Override
	public String toString() {
		String where = whereSb.toString();
		// 如果以" AND "开头,则删除" AND "
		if (where.startsWith(" AND ")) {
			where = where.substring(5);
		}
		return where;
	}

	public String getOrderBy() {
		return orderBySb.toString();
	}

	/**
	 * [获取左连接查询条件]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public String getLeftJoinWhereCondition(boolean needLeftJoin, CoreTable table) {
		StringBuffer leftJoinWhereSb = new StringBuffer();
		for (String field : fieldList) {
			// 检查该字段是否被LeftJoin标记,如果是,加入到map中
			updateFieldLeftJoinMap(field);
		}

		// 遍历所有的查询字段
		for (String field : fieldMap.keySet()) {
			if (needLeftJoin) {
				leftJoinWhereSb.append(" AND ");
				if (fieldLeftJoinMap.keySet().contains(field)) {
					// 该字段为左连接字段
					leftJoinWhereSb.append(DatabaseUtil.getReferenceTableAlias(fieldLeftJoinMap.get(field))).append(".");
					leftJoinWhereSb.append(DatabaseUtil.getReferenceValueFieldColumn(fieldLeftJoinMap.get(field)));
					leftJoinWhereSb.append(fieldValueMap.get(field));
				} else {
					// 该字段为自身字段
					leftJoinWhereSb.append(table.getTableAlias()).append(".");
					leftJoinWhereSb.append(fieldToColumn(field)).append(fieldValueMap.get(field));
				}
			} else {
				// 不考虑左连接字段
				CoreColumn column = table.getCoreColumnByFieldName(field);
				if (!column.isLeftJoinField()) {
					leftJoinWhereSb.append(" AND ");
					leftJoinWhereSb.append(fieldToColumn(field)).append(fieldValueMap.get(field));
				}
			}
		}
		String leftJoinWhere = leftJoinWhereSb.toString();
		// 如果以" AND "开头,则删除" AND "
		if (leftJoinWhere.startsWith(" AND ")) {
			leftJoinWhere = leftJoinWhere.substring(5);
		}
		return leftJoinWhere;
	}

}
