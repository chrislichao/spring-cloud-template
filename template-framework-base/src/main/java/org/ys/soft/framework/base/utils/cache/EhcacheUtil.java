package org.ys.soft.framework.base.utils.cache;

import java.net.URL;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.ys.soft.framework.base.utils.Assert;

/**
 * [单例的Ehcache缓存工具类]
 * 
 * @author Chris li[黎超]
 * @version [版本, 2017-04-12]
 * @see
 */
public class EhcacheUtil {

	private static final String PATH = "/ehcache-base.xml";

	private static final String DEFAULT_CACHE_NAME = "SystemCache";

	private static URL url;

	private volatile static CacheManager manager;

	/**
	 * [获取缓存管理类实例,双重锁确保缓存管理类单例]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	private static CacheManager getCacheManagerInstance() {
		if (manager == null) {
			synchronized (EhcacheUtil.class) {
				if (manager == null) {
					url = EhcacheUtil.class.getResource(PATH);
					manager = CacheManager.create(url);
				}
			}
		}
		return manager;
	}

	/**
	 * [通过缓存名{cacheName}获取缓存对象]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	private static Cache getCache(String cacheName) {
		Assert.notBlank(cacheName, "缓存名非法!");

		Cache cache = getCacheManagerInstance().getCache(cacheName);
		Assert.notNull(cache, "未找到对应的缓存对象[" + cacheName + "]!");
		return cache;
	}

	/**
	 * [将{key:value}存到默认缓存[DefaultCache]中]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static void put(String key, Object value) {
		put(DEFAULT_CACHE_NAME, key, value);
	}

	/**
	 * [将{key:value}存到默认缓存[DefaultCache]中,存活时间和钝化时间都为{timeToLiveSeconds}秒]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static void put(String key, Object value, int timeToLiveSeconds) {
		put(DEFAULT_CACHE_NAME, key, value, timeToLiveSeconds);
	}

	/**
	 * [将{key:value}存到默认缓存[DefaultCache]中,存活时间{timeToLiveSeconds}秒,钝化时间{
	 * timeToIdleSeconds}秒]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static void put(String key, Object value, int timeToLiveSeconds, int timeToIdleSeconds) {
		put(DEFAULT_CACHE_NAME, key, value, timeToLiveSeconds, timeToIdleSeconds);
	}

	/**
	 * [将{key:value}存到缓存{cacheName}中]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static void put(String cacheName, String key, Object value) {
		Assert.notBlank(key, "缓存Key非法!");

		synchronized (key.intern()) {
			Cache cache = getCache(cacheName);
			Element element = new Element(key, value);
			cache.put(element);
		}
	}

	/**
	 * [将{key:value}存到缓存{cacheName}中,存活时间和钝化时间都为{timeToLiveSeconds}秒]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static void put(String cacheName, String key, Object value, int timeToLiveSeconds) {
		put(cacheName, key, value, timeToLiveSeconds, timeToLiveSeconds);
	}

	/**
	 * [将{key:value}存到缓存{cacheName}中,存活时间{timeToLiveSeconds}秒,钝化时间{
	 * timeToIdleSeconds}秒]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static void put(String cacheName, String key, Object value, int timeToLiveSeconds, int timeToIdleSeconds) {
		Assert.notBlank(key, "缓存Key非法!");

		synchronized (key.intern()) {
			Cache cache = getCache(cacheName);
			Element element = new Element(key, value);
			element.setEternal(false);
			element.setTimeToLive(timeToLiveSeconds);
			element.setTimeToIdle(timeToIdleSeconds);
			cache.put(element);
		}
	}

	/**
	 * [从默认缓存[DefaultCache]中获取{key}对应的值]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static Object get(String key) {
		return get(DEFAULT_CACHE_NAME, key);
	}

	/**
	 * [从缓存{cacheName}中获取{key}对应的值]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static Object get(String cacheName, String key) {
		Assert.notBlank(key, "缓存Key非法!");

		synchronized (key.intern()) {
			Cache cache = getCache(cacheName);
			Element element = cache.get(key);
			return element == null ? null : element.getObjectValue();
		}
	}

	/**
	 * [从默认缓存[DefaultCache]中移除{key}的缓存记录]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static void remove(String key) {
		remove(DEFAULT_CACHE_NAME, key);
	}

	/**
	 * [从缓存{cacheName}中移除{key}的缓存记录]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static synchronized void remove(String cacheName, String key) {
		Assert.notBlank(key, "缓存Key非法!");

		getCache(cacheName).remove(key);
	}

}
