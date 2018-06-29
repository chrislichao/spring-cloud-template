package org.ys.soft.framework.base.model;

import java.util.List;

import org.ys.soft.framework.base.annotation.database.Column;
import org.ys.soft.framework.base.annotation.database.Id;
import org.ys.soft.framework.base.annotation.database.LeftJoin;
import org.ys.soft.framework.base.exception.FrameworkException;
import org.ys.soft.framework.base.utils.Assert;

/**
 * [核心表,供SqlBuilder类使用]
 * 
 * @author Chris li[黎超]
 * @version [版本, 2017-04-12]
 * @see CoreColumn
 */
public class CoreTable {
	private String errorMsg;
	/**
	 * 一个CoreSqlBuilder对应一个model的class
	 */
	private Class<?> clazz;
	/**
	 * 对应的数据库表名
	 */
	private String tableName;
	/**
	 * 对应的数据库表别名
	 */
	private String tableAlias;
	/**
	 * 该数据表下所有主键列
	 */
	private List<CoreColumn> pkColumnList;
	/**
	 * 该数据表下所有列
	 */
	private List<CoreColumn> columnList;
	/**
	 * 是否包含左连接字段
	 */
	private boolean containLeftJoinColumn;

	public Class<?> getClazz() {
		Assert.notNull(clazz, "Value of clazz is blank, please run 'CoreSqlBuilder.build' method first.");
		return clazz;
	}

	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}

	public String getTableName() {
		Assert.notBlank(tableName, "Value of tableName is blank, please run 'build' method first.");
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTableAlias() {
		Assert.notBlank(tableAlias, "Value of tableAlias is blank, please run 'build' method first.");
		return tableAlias;
	}

	public void setTableAlias(String tableAlias) {
		this.tableAlias = tableAlias;
	}

	public List<CoreColumn> getPkColumnList() {
		errorMsg = String.format("No field in class[%s] is presented by annotation[%s].", getClazz().getName(), Id.class.getName());
		Assert.notEmpty(pkColumnList, errorMsg);
		return pkColumnList;
	}

	public void setPkColumnList(List<CoreColumn> pkColumnList) {
		this.pkColumnList = pkColumnList;
	}

	public List<CoreColumn> getColumnList() {
		errorMsg = String.format("No field in class[%s] is presented by annotation[%s %s,or %s].", getClazz().getName(), Id.class.getName(),
				Column.class.getName(), LeftJoin.class.getName());
		Assert.notEmpty(columnList, errorMsg);
		return columnList;
	}

	public void setColumnList(List<CoreColumn> columnList) {
		this.columnList = columnList;
	}

	public void setContainLeftJoinColumn(boolean containLeftJoinColumn) {
		this.containLeftJoinColumn = containLeftJoinColumn;
	}

	public boolean isContainLeftJoinColumn() {
		return containLeftJoinColumn;
	}

	/**
	 * 通过字段名获取对应的CoreColumn对象
	 * 
	 * @param fieldName
	 * @return
	 */
	public CoreColumn getCoreColumnByFieldName(String fieldName) {
		for (CoreColumn column : columnList) {
			if (column.getFieldName().equals(fieldName)) {
				return column;
			}
		}
		// 否则,不合法,报错
		errorMsg = String.format("Field[%s] in class[%s] is not exist or is not presented by annotation[%s or %s].", fieldName, getClazz().getName(),
				Id.class.getName(), Column.class.getName());
		throw new FrameworkException(errorMsg);
	}

}
