package org.ys.soft.framework.base.utils.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ys.soft.framework.base.exception.FrameworkException;
import org.ys.soft.framework.base.utils.Assert;

/**
 * [反射工具类]
 * 
 * @author Chris li[黎超]
 * @version [版本, 2017-04-12]
 * @see
 */
public class ReflectUtil {
	private static Logger logger = LoggerFactory.getLogger(ReflectUtil.class);

	/**
	 * [利用反射获取指定对象的指定属性]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static Object getFieldValue(Object obj, String fieldName) {
		Object result = null;
		Field field = ReflectUtil.getField(obj.getClass(), Object.class, fieldName);
		try {
			field.setAccessible(true);
			result = field.get(obj);
		} catch (Exception e) {
			throw new FrameworkException(String.format("获取对象字段[%s]值出错!", fieldName), e);
		}
		return result;
	}

	/**
	 * [利用反射获取指定对象里面的指定属性]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static Field getField(Class<?> startClazz, Class<?> endClazz, String fieldName) {
		Field field = null;
		for (Class<?> clazz = startClazz; clazz != endClazz; clazz = clazz.getSuperclass()) {
			try {
				field = clazz.getDeclaredField(fieldName);
				break;
			} catch (NoSuchFieldException e) {
				logger.debug(String.format("Can not find field[%s] in class[%s],will find in it's super class.", fieldName, clazz.getName()));
			}
		}
		Assert.notNull(field, String.format("Can not find field[%s] in class[%s] and it's super classs, please check your config!", fieldName,
				startClazz.getName()));
		return field;
	}

	/**
	 * [利用反射设置指定对象的指定属性为指定的值]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static void setFieldValue(Object obj, String fieldName, Object fieldValue) {
		Field field = ReflectUtil.getField(obj.getClass(), Object.class, fieldName);
		try {
			field.setAccessible(true);
			field.set(obj, fieldValue);
		} catch (Exception e) {
			throw new FrameworkException(String.format("设置对象字段[%s]值出错!", fieldName), e);
		}
	}

	/**
	 * [获取类中所有字段(自身找完了找父类),直到父类等于endClazz为止(不包含父类)]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static Field[] getAllFields(Class<?> startClass, Class<?> endClass) {
		// 如果该类的父类为endClass,返回该类所有字段
		if (startClass.getSuperclass() == endClass) {
			return startClass.getDeclaredFields();
		}
		// 否则返回该类所有字段再找其父类的所有字段
		return (Field[]) ArrayUtils.addAll(startClass.getDeclaredFields(), getAllFields(startClass.getSuperclass(), endClass));
	}

	/**
	 * [获取类中所有方法(自身找完了找父类),直到父类等于endClazz为止(包含父类)]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static Method[] getAllMethods(Class<?> startClass, Class<?> endClass) {
		// 如果该类的父类为Object,返回该类所有方法
		if (startClass.getSuperclass() == Object.class) {
			return startClass.getDeclaredMethods();
		}
		// 如果该类的父类为endClass,返回该类和其父类的所有方法
		if (startClass.getSuperclass() == endClass) {
			return (Method[]) ArrayUtils.addAll(startClass.getDeclaredMethods(), endClass.getDeclaredMethods());
		}
		// 否则返回该类所有方法再找其父类的所有字段
		return (Method[]) ArrayUtils.addAll(startClass.getDeclaredMethods(), getAllMethods(startClass.getSuperclass(), endClass));
	}

	/**
	 * [利用反射获取指定对象里面的指定方法]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static Method getMethod(Class<?> startClazz, Class<?> endClazz, String methodName, Class<?>... parameterTypes) {
		Method method = null;
		for (Class<?> clazz = startClazz; clazz != endClazz; clazz = clazz.getSuperclass()) {
			try {
				method = clazz.getDeclaredMethod(methodName, parameterTypes);
				break;
			} catch (NoSuchMethodException e) {
				logger.debug(String.format("Can not find method[%s] in class[%s],will find in it's super class.", methodName, clazz.getName()));
			}
		}
		Assert.notNull(method, String.format("Can not find method[%s] in class[%s] and it's super classs, please check your config!", methodName,
				startClazz.getName()));
		return method;
	}
}
