package com.doosan.ddp.pm.service.sys.log
import com.doosan.ddp.pm.dao.domain.sys.log.SystemLog

interface SystemLogService {
	/**
	 *    保存日志
	 */
	void save(SystemLog log)
}
