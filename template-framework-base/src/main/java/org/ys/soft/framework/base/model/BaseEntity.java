package org.ys.soft.framework.base.model;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import org.ys.soft.framework.base.enums.database.OrderByPolicy;

/**
 * [抽象的基础实体类,用于所有实体类继承]<br>
 * 设置排序字段信息<br>
 * 1.配置注解,完成默认排序字段及排序策略;<br>
 * 2.通过方法设置排序策略,优先级高于注解;
 * 
 * @author Chris li[黎超]
 * @version [版本, 2017-04-12]
 * @see
 */
public abstract class BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 存放字段排序策略
	 */
	private Map<String, OrderByPolicy> columnSortPolicyMap = new LinkedHashMap<String, OrderByPolicy>();

	/**
	 * [添加字段排序策略,顺序]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public final BaseEntity orderByAsc(String fieldName) {
		columnSortPolicyMap.put(fieldName, OrderByPolicy.ASC);
		return this;
	}

	/**
	 * [添加字段排序策略,逆序]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public final BaseEntity orderByDesc(String fieldName) {
		columnSortPolicyMap.put(fieldName, OrderByPolicy.DESC);
		return this;
	}

}
