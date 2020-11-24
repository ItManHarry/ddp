package com.doosan.ddp.pm.service.imp.sys.log
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.doosan.ddp.pm.dao.domain.sys.log.SystemLog
import com.doosan.ddp.pm.dao.jpa.sys.log.SystemLogDao
import com.doosan.ddp.pm.service.sys.log.SystemLogService
@Service
class SystemLogServiceImp implements SystemLogService {
	
	@Autowired
	SystemLogDao systemLogDao

	@Transactional
	void save(SystemLog log) {
		// TODO Auto-generated method stub
		systemLogDao.save(log)
	}
}