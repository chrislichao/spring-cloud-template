package org.ys.soft.framework.base.annotation.database;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * [标注在实体类字段上,表的主键]<br>
 * 注:目前暂时支持单主键,后续扩展
 * 
 * @author Chris li[黎超]
 * @version [版本, 2017-04-12]
 * @see Table
 * @see Column
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Id {
	/**
	 * [主键名,指定属性对应的主键字段名]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public String name() default "";
}
