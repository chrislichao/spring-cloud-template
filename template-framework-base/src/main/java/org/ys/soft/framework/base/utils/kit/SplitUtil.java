package org.ys.soft.framework.base.utils.kit;

import java.util.ArrayList;
import java.util.List;

import org.ys.soft.framework.base.utils.Assert;

/**
 * [字符串分隔操作工具类]
 * 
 * @author Chris li[黎超]
 * @version [版本, 2017-04-12]
 * @see
 */
public class SplitUtil {

	/**
	 * [字符串数据源{source}用","分隔后,每1000个数据组合在一起,各个数据对象用","分隔]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static List<String> splitPerThousand(String source) {
		return split(source, ",", 1000, "", ",");
	}

	/**
	 * [字符串数据源{source}用","分隔后,每1000个数据组合在一起,每个数据由{around}环绕,各个数据对象用","分隔]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static List<String> splitPerThousand(String source, String around) {
		return split(source, ",", 1000, around, ",");
	}

	/**
	 * [字符串数据源{source}用{regex}分隔后,每{count}个数据组合在一起,每个数据由{around}环绕,各个数据对象用{
	 * separator}分隔]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static List<String> split(String source, String regex, int count, String around, String separator) {
		Assert.notBlank(source, "参数{source}不允许为空!");
		Assert.notBlank(regex, "参数{regex}不允许为空!");
		Assert.isTrue(count > 0, "参数{count}不允许小于等于零!");
		Assert.notNull(around, "参数{around}不允许为空!");
		Assert.notBlank(separator, "参数{separator}不允许为空!");

		List<String> splitList = new ArrayList<String>();
		StringBuffer buffer = new StringBuffer();
		String[] array = source.split(regex);
		for (int i = 0; i < array.length; i++) {
			if (i != 0 && i % count == 0) {
				splitList.add(buffer.toString().substring(1));
				buffer = new StringBuffer();
			}
			buffer.append(separator).append(around).append(array[i]).append(around);
		}
		splitList.add(buffer.toString().substring(separator.length()));

		return splitList;
	}

}
