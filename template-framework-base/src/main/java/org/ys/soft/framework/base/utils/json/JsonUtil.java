package org.ys.soft.framework.base.utils.json;

import org.ys.soft.framework.base.exception.FrameworkException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * [JSON操作工具类]
 * 
 * @author Chris li[黎超]
 * @version [版本, 2017-04-12]
 * @see
 */
public class JsonUtil {
	/**
	 * [将JavaBean对象转换成Json字符串,属性为空的字段不参与序列化]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static String beanToJson(Object object) {
		return JSONObject.toJSONString(object);
	}

	/**
	 * [将JavaBean对象转换成Json字符串,所有字段参与序列化]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static String beanToJsonAll(Object object) {
		return JSONObject.toJSONString(object, SerializerFeature.WriteMapNullValue);
	}

	/**
	 * [将Json字符串转换成JSONObject对象]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static JSONObject jsonToJsonObject(String json) {
		return JSON.parseObject(json);
	}

	/**
	 * [将Json字符串转换成JSONObject对象]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static JSONObject jsonToJsonObject(String json, String exceptionMessage) {
		try {
			return JSON.parseObject(json);
		} catch (Exception e) {
			throw new FrameworkException(exceptionMessage);
		}
	}

	/**
	 * [将Json字符串转换成JavaBean对象]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static <T> T jsonToBean(String json, Class<T> clazz) {
		return JSON.parseObject(json, clazz);
	}
}
