package org.ys.soft.framework.base.utils.id;

import java.lang.reflect.Constructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.ys.soft.framework.base.exception.FrameworkException;

/**
 * [Id工具类]
 * 
 * @author Chris li[黎超]
 * @version [版本, 2017-04-12]
 * @see
 */
@Component
public class IdUtil {

	private static final Logger logger = LoggerFactory.getLogger(IdUtil.class);

	private volatile static SnowflakeIdCreator creator;

	@Value("snowflake.data.center.id")
	private static long dataCenterId;

	@Value("snowflake.worker.id")
	private static long workerId;

	/**
	 * [获取雪花算法Id生成器实例,双重锁确保雪花算法Id生成器单例]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	private static SnowflakeIdCreator getIdCreatorInstance() {
		if (creator == null) {
			synchronized (IdUtil.class) {
				if (creator == null) {
					try {
						logger.info(String.format("初始化Id生成器,数据中心[%d],工作者[%d]", dataCenterId, workerId));
						Constructor<SnowflakeIdCreator> constructor = SnowflakeIdCreator.class.getDeclaredConstructor(long.class, long.class);
						constructor.setAccessible(true);
						creator = constructor.newInstance(dataCenterId, workerId);
					} catch (Exception e) {
						throw new FrameworkException("Id生成器工具类初始化失败!", e);
					}
				}
			}
		}
		return creator;
	}

	/**
	 * [获取下一个id,在多线程环境下也能保证不重复]
	 * 
	 * @author Chris li[黎超]
	 * @version [版本, 2017-04-12]
	 */
	public static long nextId() {
		return getIdCreatorInstance().nextId();
	}
}
