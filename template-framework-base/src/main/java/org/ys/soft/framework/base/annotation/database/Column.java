package org.ys.soft.framework.base.annotation.database;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * [标注在实体类字段上,指定对应数据库表字段]
 * 
 * @author Chris li[黎超]
 * @version [版本, 2017-04-12]
 * @see Table
 * @see Id
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {
	/**
	 * [(表)字段名,默认为属性名大写,驼峰格式]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public String name() default "";

	/**
	 * [字段数据唯一,默认为false]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public boolean unique() default false;

	/**
	 * [违反字段数据唯一时的提示信息]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public String uniqueMsgFormat() default "[%s]字段值不允许重复!";
}
