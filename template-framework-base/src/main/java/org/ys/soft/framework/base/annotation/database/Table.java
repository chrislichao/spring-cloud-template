package org.ys.soft.framework.base.annotation.database;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * [标注在实体类上,指定实体类对应的数据库表名]
 * 
 * @author Chris li[黎超]
 * @version [版本, 2017-04-12]
 * @see Id
 * @see Column
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Table {
	/**
	 * [表名,默认为实体类大写,驼峰格式]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public String name() default "";

	/**
	 * [联合主键字段(model中的字段名,逗号分隔),默认为空]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public String unionPk() default "";

	/**
	 * [违反唯一性时的提示信息]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public String unionPkMsg() default "对象已存在!";
}
