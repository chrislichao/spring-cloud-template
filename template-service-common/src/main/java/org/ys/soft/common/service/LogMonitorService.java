package org.ys.soft.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ys.soft.common.dao.LogMonitorDao;
import org.ys.soft.common.model.LogMonitor;
import org.ys.soft.framework.service.BaseDao;
import org.ys.soft.framework.service.BaseService;

/**
 * [系统监控日志服务类]
 * 
 * @author Chris li[黎超]
 * @version [版本, 2017-04-12]
 * @see
 */
@Service
public class LogMonitorService extends BaseService<LogMonitor> {

	@Autowired
	private LogMonitorDao logMonitorDao;

	@Override
	protected BaseDao<LogMonitor> getBaseDao() {
		return logMonitorDao;
	}

}
