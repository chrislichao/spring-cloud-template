package org.ys.soft.framework.base.model;

import org.ys.soft.framework.base.annotation.database.LeftJoin;
import org.ys.soft.framework.base.annotation.database.OrderBy;

/**
 * [字段(包含左连接字段),供SqlBuilder类使用]
 * 
 * @author Chris li[黎超]
 * @version [版本, 2017-04-12]
 * @see CoreTable
 */
public class CoreColumn implements Comparable<CoreColumn> {
	/**
	 * 是否数据表字段
	 */
	private boolean columnField;
	/**
	 * 字段名
	 */
	private String fieldName;
	/**
	 * 对应的表列名
	 */
	private String columnName;
	/**
	 * 是否是主键字段
	 */
	private boolean idField;
	/**
	 * 是否是左连接字段
	 */
	private boolean leftJoinField;
	/**
	 * 如果为左连接字段,该字段存放左连接配置
	 */
	private LeftJoin leftJoinConfig;
	/**
	 * 是否是排序字段
	 */
	private boolean orderByField;
	/**
	 * 如果是排序字段,该字段存放排序策略
	 */
	private OrderBy orderByConfig;

	public void setColumnField(boolean columnField) {
		this.columnField = columnField;
	}

	public boolean isColumnField() {
		return columnField;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public void setIdField(boolean idField) {
		this.idField = idField;
	}

	public boolean isIdField() {
		return idField;
	}

	public void setLeftJoinField(boolean leftJoinField) {
		this.leftJoinField = leftJoinField;
	}

	public boolean isLeftJoinField() {
		return leftJoinField;
	}

	public void setLeftJoinConfig(LeftJoin leftJoinConfig) {
		this.leftJoinConfig = leftJoinConfig;
	}

	public LeftJoin getLeftJoinConfig() {
		return leftJoinConfig;
	}

	public void setOrderByField(boolean orderByField) {
		this.orderByField = orderByField;
	}

	public boolean isOrderByField() {
		return orderByField;
	}

	public void setOrderByConfig(OrderBy orderByConfig) {
		this.orderByConfig = orderByConfig;
	}

	public OrderBy getOrderByConfig() {
		return orderByConfig;
	}

	@Override
	public int compareTo(CoreColumn column) {
		if (orderByConfig == null) {
			return 1;
		}
		if (column.orderByConfig == null) {
			return -1;
		}
		Integer old = orderByConfig.order();
		return old.compareTo(column.orderByConfig.order());
	}

}
