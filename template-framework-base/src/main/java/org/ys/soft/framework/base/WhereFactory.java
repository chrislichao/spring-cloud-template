package org.ys.soft.framework.base;

import java.lang.reflect.Constructor;

import org.ys.soft.framework.base.exception.FrameworkException;
import org.ys.soft.framework.base.model.BaseEntity;

/**
 * [自定义查询类的工厂类]
 * 
 * @author Chris li[黎超]
 * @version [版本, 2017-04-12]
 * @see
 */
public class WhereFactory {
	/**
	 * [获取一个自定义查询类的实例]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static Where createInstance(Class<? extends BaseEntity> clazz) {
		try {
			Constructor<Where> whereCls = Where.class.getDeclaredConstructor(Class.class);
			whereCls.setAccessible(true);
			return whereCls.newInstance(clazz);
		} catch (Exception e) {
			throw new FrameworkException(String.format("Get Where instance failed by class[%s]!", clazz.getName()), e);
		}
	}
}
