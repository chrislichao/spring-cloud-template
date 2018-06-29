package org.ys.soft.framework.base.annotation.database;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.ys.soft.framework.base.enums.database.Group;
import org.ys.soft.framework.base.model.BaseEntity;

/**
 * [左连接,标注在实体类字段上,该字段的值取table中的valField字段值,连接条件是标注字段和关联实体类的onField值相等]
 * 
 * @author Chris li[黎超]
 * @version [版本, 2017-04-12]
 * @see Table
 * @see Column
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface LeftJoin {
	/**
	 * [关联表(实体类)]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public Class<? extends BaseEntity> refModel();

	/**
	 * [分组,默认为1]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public Group group() default Group.ONE;

	/**
	 * [关联表(实体类)的值字段]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public String refValField();

	/**
	 * [自身表(实体类)的连接字段]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public String selfOnField();

	/**
	 * [关联表(实体类)的连接字段]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public String refOnField() default "id";
}
