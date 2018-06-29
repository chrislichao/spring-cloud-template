package org.ys.soft.framework.service.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.ys.soft.framework.service.BaseDao;
import org.ys.soft.framework.service.SelectInterceptor;

/**
 * [标注在基础查询方法上,为标注的方法自动设置返回值类型,目前仅供BaseDao使用]
 * 
 * @author Chris li[黎超]
 * @version [版本, 2017-04-12]
 * @see SelectInterceptor
 * @see BaseDao
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AutoSetResultType {
}
