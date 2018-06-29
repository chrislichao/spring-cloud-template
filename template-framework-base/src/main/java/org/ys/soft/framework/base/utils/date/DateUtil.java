package org.ys.soft.framework.base.utils.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.ys.soft.framework.base.exception.FrameworkException;
import org.ys.soft.framework.base.utils.Assert;

/**
 * [日期工具类]
 * 
 * @author Chris li[黎超]
 * @version [版本, 2017-04-12]
 * @see
 */
public class DateUtil {

	/**
	 * [默认的日期格式]
	 */
	private static final String DEFAULT_DATE_STR_FORMAT = "yyyy-MM-dd";

	/**
	 * [默认的日期时间格式]
	 */
	private static final String DEFAULT_DATETIME_STR_FORMAT = "yyyy-MM-dd HH:mm:ss";

	/**
	 * [获取指定格式的日期格式类]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	private static SimpleDateFormat getDateFormat(String pattern) {
		Assert.notBlank(pattern);
		return new SimpleDateFormat(pattern);
	}

	/**
	 * [获取指定时间{millis}的秒数,{millis}是毫秒级别的数]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	private static Integer getSeconds(long millis) {
		return Long.valueOf(millis / 1000L).intValue();
	}

	/**
	 * [获取当前时间的秒数]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static Integer getCurrentSeconds() {
		return getSeconds(System.currentTimeMillis());
	}

	/**
	 * [将秒数{seconds}转为日期]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static Date secondsToDate(Integer seconds) {
		Assert.notNull(seconds);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(seconds * 1000L);
		return calendar.getTime();
	}

	/**
	 * [将秒数{seconds}转为日期字符串,格式为{pattern}]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static String secondsToDateStr(Integer seconds, String pattern) {
		return dateToDateStr(secondsToDate(seconds), pattern);
	}

	/**
	 * [将秒数{seconds}转为日期字符串,格式为"yyyy-MM-dd"]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static String secondsToDateStr(Integer seconds) {
		return secondsToDateStr(seconds, DEFAULT_DATE_STR_FORMAT);
	}

	/**
	 * [将秒数{seconds}转为日期时间字符串,格式为"yyyy-MM-dd HH:mm:ss"]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static String secondsToDatetimeStr(Integer seconds) {
		return secondsToDateStr(seconds, DEFAULT_DATETIME_STR_FORMAT);
	}

	/**
	 * [将当前日期转为日期字符串,格式为"yyyy-MM-dd"]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static String getCurrentDateStr() {
		return dateToDateStr(new Date());
	}

	/**
	 * [将日期{date}转为日期时间字符串,格式为"yyyy-MM-dd HH:mm:ss"]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static String getCurrentDatetimeStr() {
		return dateToDatetimeStr(new Date());
	}

	/**
	 * [将日期{date}转为日期字符串,格式为"yyyy-MM-dd"]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static String dateToDateStr(Date date) {
		return dateToDateStr(date, DEFAULT_DATE_STR_FORMAT);
	}

	/**
	 * [将日期{date}转为日期时间字符串,格式为"yyyy-MM-dd HH:mm:ss"]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static String dateToDatetimeStr(Date date) {
		return dateToDateStr(date, DEFAULT_DATETIME_STR_FORMAT);
	}

	/**
	 * [将日期{date}转为日期字符串,格式为{pattern}]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static String dateToDateStr(Date date, String pattern) {
		Assert.notNull(date);
		return getDateFormat(pattern).format(date);
	}

	/**
	 * [将日期{date}转为秒]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static Integer dateToSeconds(Date date) {
		Assert.notNull(date);
		return getSeconds(date.getTime());
	}

	/**
	 * [将格式为"yyyy-MM-dd"的日期字符串{dateStr}转为日期]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static Date dateStrToDate(String dateStr) {
		return dateStrToDate(dateStr, DEFAULT_DATE_STR_FORMAT);
	}

	/**
	 * [将格式为"yyyy-MM-dd HH:mm:ss"的日期字符串{dateStr}转为日期]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static Date datetimeStrToDate(String dateStr) {
		return dateStrToDate(dateStr, DEFAULT_DATETIME_STR_FORMAT);
	}

	/**
	 * [将格式为{pattern}的日期字符串{dateStr}转为日期]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static Date dateStrToDate(String dateStr, String pattern) {
		Assert.notBlank(dateStr);
		Assert.notBlank(pattern);
		try {
			return getDateFormat(pattern).parse(dateStr);
		} catch (ParseException e) {
			throw new FrameworkException(String.format("日期字符串[%s]和日期格式[%s]不匹配!", dateStr, pattern));
		}
	}

	/**
	 * [将格式为{pattern}的日期字符串{dateStr}转为日期]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static Date dateStrToDate(String dateStr, String pattern, String exceptionMessage) {
		Assert.notBlank(dateStr);
		Assert.notBlank(pattern);
		try {
			return getDateFormat(pattern).parse(dateStr);
		} catch (ParseException e) {
			throw new FrameworkException(exceptionMessage);
		}
	}

	/**
	 * [将格式为"yyyy-MM-dd"的日期字符串{dateStr}转为秒]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static Integer dateStrToSeconds(String dateStr) {
		return dateStrToSeconds(dateStr, DEFAULT_DATE_STR_FORMAT);
	}

	/**
	 * [将格式为"yyyy-MM-dd HH:mm:ss"的日期字符串{dateStr}转为秒]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static Integer datetimeStrToSeconds(String dateStr) {
		return dateStrToSeconds(dateStr, DEFAULT_DATETIME_STR_FORMAT);
	}

	/**
	 * [将格式为{pattern}的日期字符串{dateStr}转为秒]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static Integer dateStrToSeconds(String dateStr, String pattern) {
		return dateToSeconds(dateStrToDate(dateStr, pattern));
	}
}