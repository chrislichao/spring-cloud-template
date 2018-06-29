package org.ys.soft.framework.service;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.springframework.stereotype.Component;
import org.ys.soft.framework.base.utils.reflect.ReflectUtil;
import org.ys.soft.framework.service.annotation.AutoSetResultType;

/**
 * [基础查询拦截器,只会拦截查询,主要功能:为基础查询方法统一设置ResultMap]
 * 
 * @author Chris li[黎超]
 * @version [版本, 2017-04-12]
 * @see
 */
@Component
@Intercepts( { @Signature(method = "handleResultSets", type = ResultSetHandler.class, args = { Statement.class }) })
public class SelectInterceptor implements Interceptor {
	private final static Class<?> baseDaoClass = BaseDao.class;

	/**
	 * 存放基础查询方法名
	 */
	private static List<String> autoSetResultTypeMethodList = new ArrayList<String>();

	static {
		buildAutoSetResultMapMethodList();
	}

	/**
	 * 存放BaseDao接口方法对应的EntityClass
	 */
	private Map<String, Class<?>> entityClassMap = new HashMap<String, Class<?>>();
	/**
	 * 存放EntityClass对应的ResultMap
	 */
	private Map<Class<?>, ResultMap> entityClassResultMapMap = new HashMap<Class<?>, ResultMap>();

	/**
	 * Statement statement = (Statement) invocation.getArgs()[0]; <br>
	 * ResultSet rs = statement.getResultSet();
	 */
	public Object intercept(Invocation invocation) throws Throwable {
		// 进入拦截器之后,先检查BaseDao接口中所有查询方法(被AutoSetResultType注解标注,基础查询方法,为他们统一设置ResultMap)
		ResultSetHandler resultSetHandler = (ResultSetHandler) invocation.getTarget();
		MappedStatement ms = (MappedStatement) ReflectUtil.getFieldValue(resultSetHandler, "mappedStatement");
		// 判断当前查询是否是基础查询
		if (isBaseSelectMethod(ms)) {
			// 需要过滤出基础接口中被AutoSetResultType注解标注的方法
			Class<?> entityClass = getEntityClass(ms);
			// 修改返回值类型为实体类型
			setResultType(ms, entityClass);
		}
		return invocation.proceed();
	}

	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	public void setProperties(Properties properties) {
	}

	/**
	 * [检查基础接口所有方法,发现方法被AutoSetResultType注解标注,则加入到baseSelectMethodList]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	private static void buildAutoSetResultMapMethodList() {
		for (Method method : baseDaoClass.getDeclaredMethods()) {
			if (method.isAnnotationPresent(AutoSetResultType.class)) {
				autoSetResultTypeMethodList.add(method.getName());
			}
		}

	}

	/**
	 * [判断是否是基础查询方法,如果是则需要拦截,否则不拦截]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	private boolean isBaseSelectMethod(MappedStatement ms) {
		return autoSetResultTypeMethodList.contains(getMethodName(ms));
	}

	/**
	 * [获取执行的方法名]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	private static String getMethodName(MappedStatement ms) {
		return getMethodName(ms.getId());
	}

	/**
	 * [获取执行的方法名]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	private static String getMethodName(String msId) {
		return msId.substring(msId.lastIndexOf(".") + 1);
	}

	/**
	 * [根据msId获取接口类]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	private static Class<?> getMapperClass(String msId) {
		if (msId.indexOf(".") == -1) {
			throw new RuntimeException("当前MappedStatement的id=" + msId + ",不符合MappedStatement的规则!");
		}
		String mapperClassStr = msId.substring(0, msId.lastIndexOf("."));
		try {
			return Class.forName(mapperClassStr);
		} catch (ClassNotFoundException e) {
			return null;
		}
	}

	/**
	 * [获取返回值类型 - 实体类型]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	private Class<?> getEntityClass(MappedStatement ms) {
		String msId = ms.getId();
		if (entityClassMap.containsKey(msId)) {
			return entityClassMap.get(msId);
		}
		Class<?> mapperClass = getMapperClass(msId);
		Type[] types = mapperClass.getGenericInterfaces();
		for (Type type : types) {
			if (type instanceof ParameterizedType) {
				ParameterizedType t = (ParameterizedType) type;
				if (t.getRawType() == baseDaoClass || baseDaoClass.isAssignableFrom((Class<?>) t.getRawType())) {
					Class<?> returnType = (Class<?>) t.getActualTypeArguments()[0];
					entityClassMap.put(msId, returnType);
					return returnType;
				}
			}
		}

		throw new RuntimeException("无法获取Mapper<T>泛型类型:" + msId);
	}

	/**
	 * [通过实体类,自动组装相应的ResultMap(貌似只要设置ResultMap.Builder的type为实体类的class即可)]
	 * 
	 * ResultMapping.Builder mappingBuilder = new
	 * ResultMapping.Builder(ms.getConfiguration(), "id", "ID", String.class); <br>
	 * resultMappings.add(mappingBuilder.build());
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	private ResultMap getResultMap(MappedStatement ms, Class<?> entityClass) {
		if (entityClassResultMapMap.containsKey(entityClass)) {
			return entityClassResultMapMap.get(entityClass);
		}
		List<ResultMapping> resultMappings = new ArrayList<ResultMapping>();
		ResultMap.Builder mapBuilder = new ResultMap.Builder(ms.getConfiguration(), "BaseMapperResultMap", entityClass, resultMappings, true);
		ResultMap resultMap = mapBuilder.build();
		entityClassResultMapMap.put(entityClass, resultMap);

		return resultMap;

	}

	/**
	 * [设置返回值类型 ,设置resultMap]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	private void setResultType(MappedStatement ms, Class<?> entityClass) {
		List<ResultMap> resultMaps = new ArrayList<ResultMap>();
		resultMaps.add(getResultMap(ms, entityClass));
		MetaObject metaObject = SystemMetaObject.forObject(ms);
		metaObject.setValue("resultMaps", Collections.unmodifiableList(resultMaps));
	}
}
