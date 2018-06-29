package org.ys.soft.framework.service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.jdbc.SQL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ys.soft.framework.base.Where;
import org.ys.soft.framework.base.annotation.database.LeftJoin;
import org.ys.soft.framework.base.exception.FrameworkException;
import org.ys.soft.framework.base.model.CoreColumn;
import org.ys.soft.framework.base.model.CoreTable;
import org.ys.soft.framework.base.utils.Assert;
import org.ys.soft.framework.base.utils.database.DatabaseUtil;
import org.ys.soft.framework.base.utils.reflect.ReflectUtil;

/**
 * [基础SQL自动生成器,配合CoreSqlProvider一起使用]
 * 
 * @author Chris li[黎超]
 * @version [版本, 2017-04-12]
 * @see SqlBuilder
 */
public class SqlProvider {
	Logger logger = LoggerFactory.getLogger(SqlProvider.class);

	/**
	 * 自动创建新增对象的SQL<br>
	 * 例如:<br>
	 * -----------------------------------------------------------------<br>
	 * model : id = 1, name = "Chris", remark = null <br>
	 * sql : INSERT INTO [TABLE](ID,NAME) VALUES (1,'Chris') <br>
	 * -----------------------------------------------------------------
	 */
	public String create(Object obj) {
		CoreTable table = SqlBuilder.build(obj);
		Map<String, String> insertColumnMap = SqlBuilder.getInsertColumnMap(table, obj);
		SQL sql = new SQL();
		sql.INSERT_INTO(table.getTableName());
		sql.VALUES(insertColumnMap.get(Constants.INSERT_COLUMN_NAMES_KEY), insertColumnMap.get(Constants.INSERT_COLUMN_VALUES_KEY));
		return sql.toString();
	}

	/**
	 * 基类的删除方法,通过主键删除数据<br>
	 * 例如:<br>
	 * -----------------------------------------------------------------<br>
	 * model : id = 1, name = "Chris", remark = null <br>
	 * sql : DELETE FROM [TABLE] WHERE ID = 1 <br>
	 * -----------------------------------------------------------------
	 */
	public String deleteByPk(Object obj) {
		CoreTable table = SqlBuilder.build(obj);
		SQL sql = new SQL();
		sql.DELETE_FROM(table.getTableName());
		String conditionWhere = SqlBuilder.buildPkWhere(table, false);
		if (StringUtils.isNotBlank(conditionWhere)) {
			sql.WHERE(conditionWhere);
		}
		return sql.toString();
	}

	/**
	 * 基类的批量删除方法,根据对象属性(查询条件不包括值为空的属性)删除<br>
	 * 例如:<br>
	 * -----------------------------------------------------------------<br>
	 * model : id = null, name = "Chris", remark = null <br>
	 * sql : DELETE FROM [TABLE] WHERE NAME = 'Chris' <br>
	 * -----------------------------------------------------------------
	 */
	public String deleteBatch(Object obj) {
		CoreTable table = SqlBuilder.build(obj);
		SQL sql = new SQL();
		sql.DELETE_FROM(table.getTableName());
		String conditionWhere = getWhereCondition(false, table, obj);
		if (StringUtils.isNotBlank(conditionWhere)) {
			sql.WHERE(conditionWhere);
		}
		return sql.toString();
	}

	/**
	 * 基类的批量删除方法,根据对象属性(查询条件包括值为空的属性)删除<br>
	 * 例如:<br>
	 * -----------------------------------------------------------------<br>
	 * model : id = 1, name = "Chris", remark = null <br>
	 * sql : DELETE FROM [TABLE] WHERE ID = 1 AND NAME = 'Chris' AND REMARK IS
	 * NULL<br>
	 * -----------------------------------------------------------------
	 */
	public String deleteBatchByAllFields(Object obj) {
		CoreTable table = SqlBuilder.build(obj);
		SQL sql = new SQL();
		sql.DELETE_FROM(table.getTableName());
		String conditionWhere = getWhereConditionByAllFields(false, table, obj);
		if (StringUtils.isNotBlank(conditionWhere)) {
			sql.WHERE(conditionWhere);
		}
		return sql.toString();
	}

	/**
	 * [基类的批量删除方法,自定义条件批量删除]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public String deleteBatchWhere(Where where) {
		try {
			Object obj = where.getEntityClass().newInstance();

			CoreTable table = SqlBuilder.build(obj);
			SQL sql = new SQL();
			sql.DELETE_FROM(table.getTableName());

			String conditionWhere = where.getLeftJoinWhereCondition(false, table);
			if (StringUtils.isNotBlank(conditionWhere)) {
				sql.WHERE(conditionWhere);
			}
			return sql.toString();
		} catch (Exception e) {
			throw new FrameworkException("Exception when get instance by " + where.getEntityClass().getName());
		}
	}

	/**
	 * 基类的修改方法,根据主键,修改单个对象的属性,不包含值为空的字段<br>
	 * 例如:<br>
	 * ------------------------------------------------------------------<br>
	 * model : id = 1, name = "Chris", remark = null <br>
	 * sql : UPDATE [TABLE] SET NAME = 'Chris' WHERE ID = 1 <br>
	 * ------------------------------------------------------------------
	 */
	public String updateByPk(Object obj) {
		CoreTable table = SqlBuilder.build(obj);
		SQL sql = new SQL();
		sql.UPDATE(table.getTableName());
		List<CoreColumn> columList = table.getColumnList();
		// 默认所有字段值为空
		boolean allFieldValIsNull = true;
		// 遍历columList
		for (CoreColumn column : columList) {
			if (column.isLeftJoinField()) {
				logger.warn(String.format("Field[%s] in class[%s] is left join field,will ignore it in this method[%s].", column.getFieldName(),
						table.getClazz().getName(), "updateByPk"));
			} else {
				// 判断字段的值是否为null
				Object colValue = ReflectUtil.getFieldValue(obj, column.getFieldName());
				if (null != colValue) {
					allFieldValIsNull = false;
					sql.SET(column.getColumnName() + " = #{" + column.getFieldName() + "} ");
				}
			}
		}
		Assert.isFalse(allFieldValIsNull, String.format("Are you joking? Object %s's all fields are null, how can i build sql for it?!", obj
				.getClass().getName()));
		// 获取主键部分的SQL
		String conditionWhere = SqlBuilder.buildPkWhere(table, false);
		if (StringUtils.isNotBlank(conditionWhere)) {
			sql.WHERE(conditionWhere);
		}
		return sql.toString();
	}

	/**
	 * 基类的修改方法,根据主键,修改单个对象的属性,包含值为空的字段<br>
	 * 例如:<br>
	 * ------------------------------------------------------------------------
	 * ----------------<br>
	 * model : id = 1, name = "Chris", remark = null <br>
	 * sql : UPDATE [TABLE] SET NAME = 'Chris' , REMARK IS NULL WHERE ID = 1<br>
	 * ------------------------------------------------------------------------
	 * ----------------
	 */
	public String updateAllFieldsByPk(Object obj) {
		CoreTable table = SqlBuilder.build(obj);
		SQL sql = new SQL();
		sql.UPDATE(table.getTableName());
		List<CoreColumn> columList = table.getColumnList();
		// 遍历columList
		for (CoreColumn column : columList) {
			// 判断字段的值是否为null,如果为null,则设值为null,防止出现[java.sql.SQLException:无效的列类型:1111]的异常
			if (column.isLeftJoinField()) {
				logger.warn(String.format("Field[%s] in class[%s] is left join field,will ignore it in this method[%s].", column.getFieldName(),
						table.getClazz().getName(), "updateAllFieldsByPk"));
			} else {
				Object colValue = ReflectUtil.getFieldValue(obj, column.getFieldName());
				if (null != colValue) {
					sql.SET(column.getColumnName() + " = #{" + column.getFieldName() + "} ");
				} else {
					sql.SET(column.getColumnName() + " = NULL ");
				}
			}
		}
		// 获取主键部分的SQL
		String conditionWhere = SqlBuilder.buildPkWhere(table, false);
		if (StringUtils.isNotBlank(conditionWhere)) {
			sql.WHERE(conditionWhere);
		}
		return sql.toString();
	}

	/**
	 * [基类的修改方法,根据自定义查询条件,修改单个对象的属性,不包含值为空的字段]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public String updateWhere(Map<String, Object> param) {
		Object obj = param.get("model");
		Where where = (Where) param.get("where");

		CoreTable table = SqlBuilder.build(obj);
		SQL sql = new SQL();
		sql.UPDATE(table.getTableName());
		List<CoreColumn> columList = table.getColumnList();
		// 默认所有字段值为空
		boolean allFieldValIsNull = true;
		// 遍历fieldColumnMap
		for (CoreColumn column : columList) {
			if (column.isLeftJoinField()) {
				logger.warn(String.format("Field[%s] in class[%s] is left join field,will ignore it in this method[%s].", column.getFieldName(),
						table.getClazz().getName(), "updateWhere"));
			} else {
				// 判断字段的值是否为null
				Object colValue = ReflectUtil.getFieldValue(obj, column.getFieldName());
				if (null != colValue) {
					allFieldValIsNull = false;
					sql.SET(column.getColumnName() + " = #{" + "model." + column.getFieldName() + "} ");
				}
			}
		}
		Assert.isFalse(allFieldValIsNull, String.format("Are you joking? Object %s's all fields are null, how can i build sql for it?!", obj
				.getClass().getName()));
		// 获取自定义查询部分的SQL
		String conditionWhere = where.getLeftJoinWhereCondition(false, table);
		if (StringUtils.isNotBlank(conditionWhere)) {
			sql.WHERE(conditionWhere);
		}
		return sql.toString();
	}

	/**
	 * [基类的修改方法,根据自定义查询条件,修改单个对象的属性,包含值为空的字段]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public String updateAllFieldsWhere(Map<String, Object> param) {
		Object obj = param.get("model");
		Where where = (Where) param.get("where");

		CoreTable table = SqlBuilder.build(obj);
		SQL sql = new SQL();
		sql.UPDATE(table.getTableName());
		List<CoreColumn> columList = table.getColumnList();
		// 遍历fieldColumnMap
		for (CoreColumn column : columList) {
			if (column.isLeftJoinField()) {
				logger.warn(String.format("Field[%s] in class[%s] is left join field,will ignore it in this method[%s].", column.getFieldName(),
						table.getClazz().getName(), "updateAllFieldsWhere"));
			} else {
				// 判断字段的值是否为null,如果为null,则设值为null,防止出现[java.sql.SQLException:无效的列类型:1111]的异常
				Object colValue = ReflectUtil.getFieldValue(obj, column.getFieldName());
				if (null != colValue) {
					sql.SET(column.getColumnName() + " = #{" + "model." + column.getFieldName() + "} ");
				} else {
					sql.SET(column.getColumnName() + " = NULL ");
				}
			}
		}
		// 获取自定义查询部分的SQL
		String conditionWhere = where.getLeftJoinWhereCondition(false, table);
		if (StringUtils.isNotBlank(conditionWhere)) {
			sql.WHERE(conditionWhere);
		}
		return sql.toString();
	}

	/**
	 * 基类的查询方法,通过主键查询,最多只能查到一个对象返回<如果查到多个,则抛异常<br>
	 * 例如:<br>
	 * ------------------------------------------------------------------<br>
	 * model : id = 1, name = "Chris", remark = null <br>
	 * sql : SELECT * FROM [TABLE] WHERE ID = 1 <br>
	 * ------------------------------------------------------------------
	 */
	public String retrieveByPk(Object obj) {
		CoreTable table = SqlBuilder.build(obj);
		SQL sql = new SQL();
		String conditionWhere = "";
		sql.SELECT(SqlBuilder.buildRetrieveColumnName(table));
		// 判断是否存在字段LeftJoin
		if (table.isContainLeftJoinColumn()) {
			// 存在左连接
			buildLeftJoinSql(table, sql);
			conditionWhere = SqlBuilder.buildPkWhere(table, true);
		} else {
			// 没有左连接字段
			sql.FROM(table.getTableName());
			conditionWhere = SqlBuilder.buildPkWhere(table, false);
		}
		if (StringUtils.isNotBlank(conditionWhere)) {
			sql.WHERE(conditionWhere);
		}
		return sql.toString();
	}

	/**
	 * [构建左连接SQL语句(部分)]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	private void buildLeftJoinSql(CoreTable table, SQL sql) {
		sql.FROM(table.getTableName() + " " + table.getTableAlias());
		// 存放存在的表名及分组号,防止重复添加
		Set<String> tableGroup = new HashSet<String>();
		for (CoreColumn column : table.getColumnList()) {
			// 是左连接字段,并且未出现过
			if (column.isLeftJoinField()) {
				String leftJoinKey = getLeftJoinKey(column.getLeftJoinConfig());
				if (!tableGroup.contains(leftJoinKey)) {
					tableGroup.add(leftJoinKey);
					String refTableAlias = DatabaseUtil.getReferenceTableAlias(column.getLeftJoinConfig());
					StringBuffer joinSb = new StringBuffer();
					joinSb.append(DatabaseUtil.getReferenceTableName(column.getLeftJoinConfig().refModel())).append(" ");
					joinSb.append(refTableAlias).append(" ON ").append(table.getTableAlias()).append(".");
					joinSb.append(DatabaseUtil.getSelfOnFieldColumn(table.getClazz(), column.getLeftJoinConfig()));
					joinSb.append(" = ").append(refTableAlias).append(".").append(DatabaseUtil.getReferenceOnFieldColumn(column.getLeftJoinConfig()));
					sql.LEFT_OUTER_JOIN(joinSb.toString());
				}
			}
		}
	}

	/**
	 * [获取左连接配置的表名和分组拼接]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	private String getLeftJoinKey(LeftJoin leftJoinConfig) {
		return leftJoinConfig.refModel().getName() + "." + leftJoinConfig.group().getValue();
	}

	/**
	 * 基类的查询方法,根据对象属性(查询条件不包括值为空的属性)查到对象集合 例如:<br>
	 * ------------------------------------------------------------------<br>
	 * model : id = 1, name = "Chris", remark = null <br>
	 * sql : SELECT * FROM [TABLE] WHERE ID = 1 AND NAME = 'Chris'<br>
	 * ------------------------------------------------------------------
	 */
	public String retrieveList(Object obj) {
		CoreTable table = SqlBuilder.build(obj);
		SQL sql = new SQL();
		String conditionWhere = "";
		sql.SELECT(SqlBuilder.buildRetrieveColumnName(table));
		// 判断是否存在字段LeftJoin
		if (table.isContainLeftJoinColumn()) {
			// 存在左连接
			buildLeftJoinSql(table, sql);
			conditionWhere = getWhereCondition(true, table, obj);
		} else {
			// 没有左连接字段
			sql.FROM(table.getTableName());
			conditionWhere = getWhereCondition(false, table, obj);
		}
		if (StringUtils.isNotBlank(conditionWhere)) {
			sql.WHERE(conditionWhere);
		}
		String orderBy = SqlBuilder.buildOrderByColumnsInfo(table, obj);
		if (StringUtils.isNotBlank(orderBy)) {
			sql.ORDER_BY(orderBy);
		}
		return sql.toString();
	}

	/**
	 * 基类的查询方法,根据对象属性(查询条件包括值为空的属性)查到对象集合 例如:<br>
	 * ------------------------------------------------------------------<br>
	 * model : id = 1, name = "Chris", remark = null <br>
	 * sql : SELECT * FROM [TABLE] WHERE ID = 1 AND NAME = 'Chris' AND REMARK IS
	 * NULL<br>
	 * ------------------------------------------------------------------
	 */
	public String retrieveListByAllFields(Object obj) {
		CoreTable table = SqlBuilder.build(obj);
		SQL sql = new SQL();
		String conditionWhere = "";
		sql.SELECT(SqlBuilder.buildRetrieveColumnName(table));
		// 判断是否存在字段LeftJoin
		if (table.isContainLeftJoinColumn()) {
			// 存在左连接
			buildLeftJoinSql(table, sql);
			conditionWhere = getWhereConditionByAllFields(true, table, obj);
		} else {
			// 没有左连接字段
			sql.FROM(table.getTableName());
			conditionWhere = getWhereConditionByAllFields(false, table, obj);
		}
		if (StringUtils.isNotBlank(conditionWhere)) {
			sql.WHERE(conditionWhere);
		}

		String orderBy = SqlBuilder.buildOrderByColumnsInfo(table, obj);
		if (StringUtils.isNotBlank(orderBy)) {
			sql.ORDER_BY(orderBy);
		}
		return sql.toString();
	}

	/**
	 * 基类的查询方法,根据自定义查询条件查到对象集合
	 */
	public String retrieveListWhere(Where where) {
		try {
			Object obj = where.getEntityClass().newInstance();

			CoreTable table = SqlBuilder.build(obj);
			SQL sql = new SQL();
			String conditionWhere = "";
			sql.SELECT(SqlBuilder.buildRetrieveColumnName(table));
			// 判断是否存在字段LeftJoin
			if (table.isContainLeftJoinColumn()) {
				// 存在左连接,则需要重新组装SQL语句
				buildLeftJoinSql(table, sql);
				conditionWhere = where.getLeftJoinWhereCondition(true, table);
			} else {
				sql.FROM(table.getTableName());
				conditionWhere = where.toString();
			}
			if (StringUtils.isNotBlank(conditionWhere)) {
				sql.WHERE(conditionWhere);
			}
			String orderBy = where.getOrderBy();
			if (StringUtils.isNotBlank(orderBy)) {
				sql.ORDER_BY(orderBy);
			}
			return sql.toString();
		} catch (Exception e) {
			throw new FrameworkException("Exception when get instance by " + where.getEntityClass().getName());
		}
	}

	/**
	 * 基类的查询个数方法,根据对象属性(查询条件不包括值为空的属性)查到对象个数 例如:<br>
	 * ------------------------------------------------------------------<br>
	 * model : id = 1, name = "Chris", remark = null <br>
	 * sql : SELECT COUNT(1) FROM [TABLE] WHERE ID = 1 AND NAME = 'Chris'<br>
	 * ------------------------------------------------------------------
	 */
	public String getCount(Object obj) {
		CoreTable table = SqlBuilder.build(obj);
		String conditionWhere = "";
		SQL sql = new SQL();
		sql.SELECT("COUNT(1)");
		// 判断是否存在字段LeftJoin
		if (table.isContainLeftJoinColumn()) {
			// 存在左连接
			buildLeftJoinSql(table, sql);
			conditionWhere = getWhereCondition(true, table, obj);
		} else {
			// 没有左连接字段
			sql.FROM(table.getTableName());
			conditionWhere = getWhereCondition(false, table, obj);
		}
		if (StringUtils.isNotBlank(conditionWhere)) {
			sql.WHERE(conditionWhere);
		}
		String orderBy = SqlBuilder.buildOrderByColumnsInfo(table, obj);
		if (StringUtils.isNotBlank(orderBy)) {
			sql.ORDER_BY(orderBy);
		}
		return sql.toString();
	}

	/**
	 * 基类的查询个数方法,根据对象属性(查询条件包括值为空的属性)查到对象个数 例如:<br>
	 * ------------------------------------------------------------------<br>
	 * model : id = 1, name = "Chris", remark = null <br>
	 * sql : SELECT COUNT(1) FROM [TABLE] WHERE ID = 1 AND NAME = 'Chris' AND
	 * REMARK IS NULL<br>
	 * ------------------------------------------------------------------
	 */
	public String getCountByAllFields(Object obj) {
		CoreTable table = SqlBuilder.build(obj);
		String conditionWhere = "";
		SQL sql = new SQL();
		sql.SELECT("COUNT(1)");
		// 判断是否存在字段LeftJoin
		if (table.isContainLeftJoinColumn()) {
			// 存在左连接
			buildLeftJoinSql(table, sql);
			conditionWhere = getWhereConditionByAllFields(true, table, obj);
		} else {
			// 没有左连接字段
			sql.FROM(table.getTableName());
			conditionWhere = getWhereConditionByAllFields(false, table, obj);
		}
		if (StringUtils.isNotBlank(conditionWhere)) {
			sql.WHERE(conditionWhere);
		}
		String orderBy = SqlBuilder.buildOrderByColumnsInfo(table, obj);
		if (StringUtils.isNotBlank(orderBy)) {
			sql.ORDER_BY(orderBy);
		}
		return sql.toString();
	}

	/**
	 * [基类的查询个数方法,根据自定义查询条件查到对象个数]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public String getCountWhere(Where where) {
		try {
			Object obj = where.getEntityClass().newInstance();
			String conditionWhere = "";
			CoreTable table = SqlBuilder.build(obj);
			SQL sql = new SQL();
			sql.SELECT("COUNT(1)");
			// 判断是否存在字段LeftJoin
			if (table.isContainLeftJoinColumn()) {
				// 存在左连接
				buildLeftJoinSql(table, sql);
				conditionWhere = where.getLeftJoinWhereCondition(true, table);
			} else {
				// 没有左连接字段
				sql.FROM(table.getTableName());
				conditionWhere = where.toString();
			}
			if (StringUtils.isNotBlank(conditionWhere)) {
				sql.WHERE(conditionWhere);
			}
			String orderBy = where.getOrderBy();
			if (StringUtils.isNotBlank(orderBy)) {
				sql.ORDER_BY(orderBy);
			}
			return sql.toString();
		} catch (Exception e) {
			throw new FrameworkException("Exception when get instance by " + where.getEntityClass().getName());
		}
	}

	/**
	 * [解析obj对象,获取属性值不为空的属性及属性值拼装为where查询条件]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	private String getWhereCondition(boolean needLeftJoin, CoreTable table, Object obj) {
		// 所有字段
		List<CoreColumn> columList = table.getColumnList();
		StringBuffer whereSb = new StringBuffer();
		// 遍历columList
		for (CoreColumn column : columList) {
			// 判断字段的值是否为null
			Object colValue = ReflectUtil.getFieldValue(obj, column.getFieldName());
			if (null != colValue) {
				if (needLeftJoin) {
					whereSb.append(" AND ");
					if (column.isLeftJoinField()) {
						// 该字段为左连接字段
						whereSb.append(DatabaseUtil.getReferenceTableAlias(column.getLeftJoinConfig())).append(".");
						whereSb.append(DatabaseUtil.getReferenceValueFieldColumn(column.getLeftJoinConfig()));
					} else {
						// 该字段非左连接字段
						whereSb.append(table.getTableAlias()).append(".").append(column.getColumnName());
					}
					whereSb.append(" = #{").append(column.getFieldName()).append("} ");
				} else {
					if (column.isLeftJoinField()) {
						logger.warn(String.format("Field[%s] in class[%s] is left join field,will ignore it in method[%s].", column.getFieldName(),
								table.getClazz().getName(), "getWhereCondition"));
					} else {
						whereSb.append(" AND ");
						whereSb.append(column.getColumnName());
						whereSb.append(" = #{").append(column.getFieldName()).append("} ");
					}
				}
			}
		}
		String whereCondition = whereSb.toString();
		if (!whereCondition.equals("")) {
			// 有值则去掉第一个" AND "
			whereCondition = whereCondition.substring(5);
		}
		return whereCondition;
	}

	/**
	 * [解析obj对象,获取所有属性及属性值拼装为where查询条件]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	private String getWhereConditionByAllFields(boolean needLeftJoin, CoreTable table, Object obj) {
		// 所有字段
		List<CoreColumn> columList = table.getColumnList();
		StringBuffer whereSb = new StringBuffer();
		// 遍历columList
		for (CoreColumn column : columList) {
			boolean ignore = false; // 用来标记是否忽略该字段
			if (needLeftJoin) {
				whereSb.append(" AND ");
				if (column.isLeftJoinField()) {
					// 该字段为左连接字段
					whereSb.append(DatabaseUtil.getReferenceTableAlias(column.getLeftJoinConfig())).append(".");
					whereSb.append(DatabaseUtil.getReferenceValueFieldColumn(column.getLeftJoinConfig()));
				} else {
					// 该字段非左连接字段
					whereSb.append(table.getTableAlias()).append(".").append(column.getColumnName());
				}
			} else {
				if (column.isLeftJoinField()) {
					ignore = true;
					logger.warn(String.format("Field[%s] in class[%s] is left join field,will ignore it in this method[%s].", column.getFieldName(),
							table.getClazz().getName(), "getWhereConditionByAllFields"));
				} else {
					whereSb.append(" AND ");
					whereSb.append(column.getColumnName());
				}
			}
			if (!ignore) {
				// 判断字段的值是否为null
				Object colValue = ReflectUtil.getFieldValue(obj, column.getFieldName());
				if (null != colValue) {
					whereSb.append(" = #{" + column.getFieldName() + "} ");
				} else {
					whereSb.append(" IS NULL ");
				}
			}
		}
		String whereCondition = whereSb.toString();
		if (!whereCondition.equals("")) {
			// 有值则去掉第一个" AND "
			whereCondition = whereCondition.substring(5);
		}
		return whereCondition;
	}
}
