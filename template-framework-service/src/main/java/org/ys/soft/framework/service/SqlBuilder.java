package org.ys.soft.framework.service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ys.soft.framework.base.annotation.database.Column;
import org.ys.soft.framework.base.annotation.database.Id;
import org.ys.soft.framework.base.annotation.database.LeftJoin;
import org.ys.soft.framework.base.annotation.database.OrderBy;
import org.ys.soft.framework.base.annotation.database.Table;
import org.ys.soft.framework.base.enums.database.OrderByPolicy;
import org.ys.soft.framework.base.model.BaseEntity;
import org.ys.soft.framework.base.model.CoreColumn;
import org.ys.soft.framework.base.model.CoreTable;
import org.ys.soft.framework.base.utils.Assert;
import org.ys.soft.framework.base.utils.database.DatabaseUtil;
import org.ys.soft.framework.base.utils.reflect.ReflectUtil;

/**
 * [基础SQL构建器,配合CoreSqlProvider一起使用]
 * 
 * @author Chris li[黎超]
 * @version [版本, 2017-04-12]
 * @see SqlProvider
 */
public class SqlBuilder {
	private static Log logger = LogFactory.getLog(SqlBuilder.class);

	// 存放model类对应的CoreTable
	private static Map<Class<? extends BaseEntity>, CoreTable> coreTableMap = new HashMap<Class<? extends BaseEntity>, CoreTable>();

	// 存放错误信息
	private static String errorMsg;

	/**
	 * [给指定的model类,生成一个CoreTable对象]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	@SuppressWarnings("unchecked")
	public static CoreTable build(Object object) {
		Assert.notNull(object);
		Class<? extends BaseEntity> clazz = (Class<? extends BaseEntity>) object.getClass();
		// 先从builderMap中获取,如果能取到,则直接返回
		CoreTable coreTable = coreTableMap.get(clazz);
		if (coreTable != null) {
			return coreTable;
		}
		// 否则,重新生成,完成后存入builderMap中,方便下次使用
		coreTable = buildCoreTable(clazz);

		// 最后将该类对应的builder加入到map中
		coreTableMap.put(clazz, coreTable);
		return coreTable;
	}

	/**
	 * [根据实体类的class生成一个新的CoreTable]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	private static CoreTable buildCoreTable(Class<? extends BaseEntity> clazz) {
		CoreTable coreTable = new CoreTable();
		coreTable.setClazz(clazz);
		// 设置表名
		buildTableName(clazz, coreTable);
		// 设置model字段和数据库表列对应关系,自动生成默认插入SQL语句相关信息,同时设置model字段排序策略
		buildColumnList(clazz, coreTable);
		return coreTable;
	}

	/**
	 * [设置表名和表别名]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	private static void buildTableName(Class<? extends BaseEntity> clazz, CoreTable coreTable) {
		// 判断对象是否被Table注解标注
		Assert.isAnnotationPresent(Table.class, clazz);
		// 设置Table表名
		Table table = clazz.getAnnotation(Table.class);
		String tableName = table.name();
		if (StringUtils.isNotBlank(tableName)) {
			coreTable.setTableName(DatabaseUtil.switchByDatabaseCaseType(tableName));
		} else {
			coreTable.setTableName(DatabaseUtil.switchForDatebase(clazz.getSimpleName()));
		}
		coreTable.setTableAlias(DatabaseUtil.getTableAlias(coreTable.getTableName()));
	}

	/**
	 * [设置model字段和数据库表列对应关系]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	private static void buildColumnList(Class<? extends BaseEntity> clazz, CoreTable coreTable) {
		// 获取实体类所有字段
		Field[] allFields = ReflectUtil.getAllFields(clazz, BaseEntity.class);
		// 排序顺序的集合,用来区分重复
		List<String> sortOrderList = new ArrayList<String>();
		// 主键列集合
		List<CoreColumn> pkColumnList = new ArrayList<CoreColumn>();
		// 数据列集合
		List<CoreColumn> columnList = new ArrayList<CoreColumn>();
		for (Field field : allFields) {
			executeBuild(clazz, coreTable, sortOrderList, pkColumnList, columnList, field);
		}
		// 设置字段集合
		coreTable.setPkColumnList(pkColumnList);
		coreTable.setColumnList(columnList);
		// 为OrderBy字段排序
		Collections.sort(coreTable.getColumnList());
	}

	/**
	 * [执行生成]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	private static void executeBuild(Class<? extends BaseEntity> clazz, CoreTable coreTable, List<String> sortOrderList,
			List<CoreColumn> pkColumnList, List<CoreColumn> columnList, Field field) {
		// 首先定义一个字段
		CoreColumn column = new CoreColumn();
		// 默认不是数据表的字段
		column.setColumnField(false);

		String columnName = DatabaseUtil.switchForDatebase(field.getName());
		// 判断字段是否被Id注解标注,如果是,则表示该字段对应数据表中某主键列
		if (field.isAnnotationPresent(Id.class)) {
			Id id = field.getAnnotation(Id.class);
			if (StringUtils.isNotBlank(id.name())) {
				columnName = DatabaseUtil.switchByDatabaseCaseType(id.name());
			}
			column.setColumnField(true);
			column.setFieldName(field.getName());
			column.setColumnName(columnName);
			column.setIdField(true);
			column.setLeftJoinField(false);
		}

		// 判断字段是否被Column注解标注,如果是,则表示该字段对应数据表中某列
		if (field.isAnnotationPresent(Column.class)) {
			if (!column.isColumnField()) {
				// 未被Id注解标注过,Column注解才生效
				Column col = field.getAnnotation(Column.class);
				if (StringUtils.isNotBlank(col.name())) {
					columnName = DatabaseUtil.switchByDatabaseCaseType(col.name());
				}
				column.setColumnField(true);
				column.setFieldName(field.getName());
				column.setColumnName(columnName);
				column.setIdField(false);
				column.setLeftJoinField(false);
			} else {
				// 既被Id注解标注,又被Column注解标注,以Id标注的为准
				errorMsg = String.format("Field[%s] in Class[%s] is annotation present by both [Id] and [Column],only [Id] is effective.", field
						.getName(), clazz.getName());
				logger.error(errorMsg);
			}
		}

		// 判断字段是否被LeftJoin注解标注,如果是,则表示该字段跟其他表关联
		if (field.isAnnotationPresent(LeftJoin.class)) {
			if (!column.isColumnField()) {
				// 未被Id和Column注解标注过,LeftJoin注解才生效
				LeftJoin leftJoin = field.getAnnotation(LeftJoin.class);

				column.setColumnField(true);
				column.setFieldName(field.getName());
				column.setColumnName(columnName);
				column.setIdField(false);
				column.setLeftJoinField(true);
				column.setLeftJoinConfig(leftJoin);

				coreTable.setContainLeftJoinColumn(true);
			} else {
				// 如果字段被Id或Column注解标注后,LeftJoin标记将无效
				errorMsg = String.format("Field[%s] in Class[%s] is annotation present by [Id] or [Column],[LeftJoin] will not be effective.", field
						.getName(), clazz.getName());
				logger.error(errorMsg);
			}
		}

		// 判断字段是否被OrderBy注解标注,如果是,则表示该字段需要设置默认排序策略
		if (field.isAnnotationPresent(OrderBy.class)) {
			if (column.isColumnField()) {
				// 必须先被Id或者Column或者LeftJoin注解标注过,OrderBy注解才生效
				OrderBy orderBy = field.getAnnotation(OrderBy.class);
				// 如果已经包含了该顺序,需要提示下,第一次出现的有效
				if (sortOrderList.contains(String.valueOf(orderBy.order()))) {
					errorMsg = String.format("OrderBy in Class[%s] is repeat where order = [%d],the first one is effective.", clazz.getName(),
							orderBy.order());
					logger.error(errorMsg);
				} else {
					sortOrderList.add(String.valueOf(orderBy.order()));
					column.setOrderByField(true);
					column.setOrderByConfig(orderBy);
				}
			} else {
				errorMsg = String.format("Field[%s] in Class[%s] is annotation present by only [OrderBy],you must also add [Id] or [Column].", field
						.getName(), clazz.getName());
				logger.error(errorMsg);
			}
		}
		if (column.isIdField()) {
			pkColumnList.add(column);
		}
		if (column.isColumnField()) {
			columnList.add(column);
		}
	}

	/**
	 * [获取obj对应数据表默认的插入语句字段名]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static Map<String, String> getInsertColumnMap(CoreTable coreTable, Object obj) {
		Map<String, String> insertColumnMap = new HashMap<String, String>();
		// 存放Insert SQL 语句字段名信息
		StringBuffer defaultInsertColumnNameBuffer = new StringBuffer();
		// 存放Insert SQL 语句字段值信息
		StringBuffer defaultInsertColumnValueBuffer = new StringBuffer();

		boolean allValueIsNull = true;
		// 遍历fieldColumnMap
		for (CoreColumn column : coreTable.getColumnList()) {
			// 如果该栏位有值,则插入
			Object colValue = ReflectUtil.getFieldValue(obj, column.getFieldName());
			if (null != colValue && !column.isLeftJoinField()) {
				allValueIsNull = false;
				defaultInsertColumnNameBuffer.append(",").append(column.getColumnName());
				defaultInsertColumnValueBuffer.append(",#{").append(column.getFieldName()).append("}");
			}
		}
		// 如果obj所有插入数据库的字段都为null,则无法生存sql语句,报错
		Assert.isFalse(allValueIsNull, "All value of Object is null,can not build insert sql.");

		insertColumnMap.put(Constants.INSERT_COLUMN_NAMES_KEY, defaultInsertColumnNameBuffer.toString().substring(1));
		insertColumnMap.put(Constants.INSERT_COLUMN_VALUES_KEY, defaultInsertColumnValueBuffer.toString().substring(1));

		return insertColumnMap;
	}

	/**
	 * [生成主键where语句]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static String buildPkWhere(CoreTable coreTable, boolean needLeftJoin) {
		StringBuffer sb = new StringBuffer();
		// 遍历pkColumnMap
		boolean first = true;
		for (CoreColumn pkColumn : coreTable.getPkColumnList()) {
			if (first) {
				first = false;
			} else {
				sb.append(" AND ");
			}
			// 左连接要带上别名
			if (needLeftJoin) {
				sb.append(coreTable.getTableAlias()).append(".");
			}

			sb.append(pkColumn.getColumnName()).append(" = #{").append(pkColumn.getFieldName()).append("}");
		}
		return sb.toString();
	}

	/**
	 * [生成检索字段列]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static String buildRetrieveColumnName(CoreTable coreTable) {
		StringBuffer sb = new StringBuffer();
		// 遍历fieldColumnMap
		boolean first = true;
		for (CoreColumn column : coreTable.getColumnList()) {
			if (first) {
				first = false;
			} else {
				sb.append(" , ");
			}

			// 判断是否包含左连接字段
			if (coreTable.isContainLeftJoinColumn()) {
				if (column.isLeftJoinField()) {
					sb.append(DatabaseUtil.getReferenceTableAlias(column.getLeftJoinConfig()));
					sb.append(".").append(DatabaseUtil.getReferenceValueFieldColumn(column.getLeftJoinConfig()));
				} else {
					sb.append(coreTable.getTableAlias()).append(".").append(column.getColumnName());
				}
			} else {
				sb.append(column.getColumnName());
			}
			sb.append(" AS ").append(column.getFieldName());
		}
		return sb.toString();
	}

	/**
	 * 获取排序栏位字段及策略
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String buildOrderByColumnsInfo(CoreTable coreTable, Object obj) {
		// 定义排序策略有两种方式,1.在字段上加注解,2.通过方法指定策略(优先级更高)
		StringBuffer orderByColumnsInfoSb = new StringBuffer();
		// 判断是否通过方法指定排序方式
		Map<String, OrderByPolicy> columnSortPolicyMap = (Map<String, OrderByPolicy>) ReflectUtil.getFieldValue(obj, "columnSortPolicyMap");
		if (columnSortPolicyMap.isEmpty()) {
			// 如果没有通过方法指定排序,则取字段上定义的排序方式
			for (CoreColumn column : coreTable.getColumnList()) {
				if (column.isOrderByField()) {
					orderByColumnsInfoSb.append(",");
					orderByColumnsInfoSb.append(column.getFieldName());
					orderByColumnsInfoSb.append(" ");
					orderByColumnsInfoSb.append(column.getOrderByConfig().policy());
				}
			}
		} else {
			// 以columnSortPolicyMap排序方式为主
			Set<String> keySet = columnSortPolicyMap.keySet();
			Iterator<String> iter = keySet.iterator();
			while (iter.hasNext()) {
				String key = iter.next();
				CoreColumn column = coreTable.getCoreColumnByFieldName(key);
				// 如果包含,则是合法字段
				orderByColumnsInfoSb.append(",").append(column.getFieldName()).append(" ").append(columnSortPolicyMap.get(key).toString());
			}
		}
		// 如果有排序信息,截取第一个","
		return orderByColumnsInfoSb.toString().equals("") ? "" : orderByColumnsInfoSb.toString().substring(1);
	}

}
