package org.ys.soft.framework.base.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * [接口访问限流的注解,标注在Controller方法上,如果超过接口访问的QPS,则限制本接口访问]
 * 
 * @author Chris li[黎超]
 * @version [版本, 2017-04-12]
 * @see
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RateLimit {
	/**
	 * [接口访问限流的时间周期,默认为1s]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	long timeout() default 1;

	/**
	 * [一个时间周期内限定的访问次数,默认为200]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	int count() default 200;
}
