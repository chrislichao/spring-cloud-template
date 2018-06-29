package org.ys.soft.framework.base.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * [监控的注解,标注在Controller方法上,监控该方法调用的相关信息]
 * 
 * @author Chris li[黎超]
 * @version [版本, 2017-04-12]
 * @see
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Monitor {
	/**
	 * [被监控的方法描述]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	String description();

	/**
	 * [是否存储在数据库中,默认为true]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	boolean storeInDatabase() default true;

}
