package org.ys.soft.framework.base.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.ys.soft.framework.base.enums.system.Action;
import org.ys.soft.framework.base.enums.system.Menu;

/**
 * [验证权限的注解,标注在方法上,调用该方法时,当前用户必须为登录状态,并根据effective的值确定是否需要验证权限]
 * 
 * @author Chris li[黎超]
 * @version [版本, 2017-04-12]
 * @see
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ValidatePermission {
	/**
	 * [是否有效,如果为true,则需验证该方法的权限]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	boolean effective() default true;

	/**
	 * [所属菜单]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	Menu menu();

	/**
	 * [关联操作]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	Action action();
}
