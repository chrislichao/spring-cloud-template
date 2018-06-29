package org.ys.soft.framework.base.annotation.database;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.ys.soft.framework.base.enums.database.OrderByPolicy;

/**
 * [标注在实体类字段上,指定查询时排序策略]
 * 
 * @author Chris li[黎超]
 * @version [版本, 2017-04-12]
 * @see Table
 * @see Id
 * @see Column
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface OrderBy {
	/**
	 * [排序顺序(从1开始)]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public int order();

	/**
	 * [排序策略,默认为顺序]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public OrderByPolicy policy() default OrderByPolicy.ASC;
}
