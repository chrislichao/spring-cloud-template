package org.ys.soft.framework.base.utils;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.ys.soft.framework.base.exception.FrameworkException;

/**
 * [断言工具类]
 * 
 * @author Chris li[黎超]
 * @version [版本, 2017-04-12]
 * @see
 */
public class Assert {
	/**
	 * 正整数类型的分隔格式
	 */
	public final static String POSITIVE_INTEGER_TYPE_FORMAT = "^[1-9]\\d*(,[1-9]\\d*)*$";

	/**
	 * [断言表达式为真]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static void isTrue(boolean expression) {
		isTrue(expression, "[Assertion failed] - this expression must be true!");
	}

	/**
	 * [断言表达式为真]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static void isTrue(boolean expression, String message) {
		if (!expression) {
			throw new FrameworkException(message);
		}
	}

	/**
	 * [断言表达式为假]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static void isFalse(boolean expression) {
		isTrue(expression, "[Assertion failed] - this expression must be false!");
	}

	/**
	 * [断言表达式为假]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static void isFalse(boolean expression, String message) {
		if (expression) {
			throw new FrameworkException(message);
		}
	}

	/**
	 * [断言参数不为空]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static void notNull(Object object) {
		notNull(object, "[Assertion failed] - the object argument must not be null!");
	}

	/**
	 * [断言参数不为空]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static void notNull(Object object, String message) {
		if (object == null) {
			throw new FrameworkException(message);
		}
	}

	/**
	 * [断言参数为空或为空白字符串]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static void isBlank(String text) {
		isBlank(text, "[Assertion failed] - this String argument must be null, empty, or blank!");
	}

	/**
	 * [断言参数为空或为空白字符串]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static void isBlank(String text, String message) {
		if (StringUtils.isNotBlank(text)) {
			throw new FrameworkException(message);
		}
	}

	/**
	 * [断言参数不为空,且不为空白字符串]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static void notBlank(String text) {
		notBlank(text, "[Assertion failed] - this String argument must not be null, empty, or blank!");
	}

	/**
	 * [断言参数不为空,且不为空白字符串]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static void notBlank(String text, String message) {
		if (StringUtils.isBlank(text)) {
			throw new FrameworkException(message);
		}
	}

	/**
	 * [断言参数集合不为空]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static void notEmpty(Collection<?> collection) {
		notEmpty(collection, "[Assertion failed] - this collection must not be empty: it must contain at least 1 element!");
	}

	/**
	 * [断言参数集合不为空]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static void notEmpty(Collection<?> collection, String message) {
		if (CollectionUtils.isEmpty(collection)) {
			throw new FrameworkException(message);
		}
	}

	/**
	 * [断言参数集合不为空]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static void notEmpty(Map<?, ?> map) {
		notEmpty(map, "[Assertion failed] - this map must not be empty; it must contain at least one entry!");
	}

	/**
	 * [断言参数集合不为空]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static void notEmpty(Map<?, ?> map, String message) {
		if (map == null || map.isEmpty()) {
			throw new FrameworkException(message);
		}
	}

	/**
	 * [断言{clazz}被{annotationClass}注解标注]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static void isAnnotationPresent(Class<? extends Annotation> annotationClass, Class<?> clazz) {
		isAnnotationPresent(annotationClass, clazz, String.format("[Assertion failed] - class[%s] must be annotation present by [%s]!", clazz
				.getName(), annotationClass.getName()));
	}

	/**
	 * [断言{clazz}被{annotationClass}注解标注]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static void isAnnotationPresent(Class<? extends Annotation> annotationClass, Class<?> clazz, String message) {
		notNull(annotationClass);
		notNull(clazz);
		if (!clazz.isAnnotationPresent(annotationClass)) {
			throw new FrameworkException(message);
		}
	}

	/**
	 * [断言{source}匹配Ids格式[id1,id2,...,idn],其中idn是正整数]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static void isMatchedIdsFormat(String source) {
		isMatchedIdsFormat(source, String.format(
				"[Assertion failed] - [%s] is not matched ids format [id1,id2,id3,...,idn], which {idn} is positive integer!", source));
	}

	/**
	 * [断言{source}匹配Ids格式[id1,id2,...,idn],其中idn是正整数]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static void isMatchedIdsFormat(String source, String message) {
		notBlank(source);
		if (!Pattern.matches(POSITIVE_INTEGER_TYPE_FORMAT, source)) {
			throw new FrameworkException(message);
		}
	}

}
