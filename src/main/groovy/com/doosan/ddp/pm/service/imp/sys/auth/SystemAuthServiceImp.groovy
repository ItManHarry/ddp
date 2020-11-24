package com.doosan.ddp.pm.service.imp.sys.auth
import org.springframework.beans.factory.annotation.Autowired
import com.doosan.ddp.pm.service.sys.auth.SystemAuthService 
import org.springframework.stereotype.Service
import com.doosan.ddp.pm.dao.domain.sys.auth.SystemAuth
import com.doosan.ddp.pm.dao.jpa.sys.auth.SystemAuthDao
@Service
class SystemAuthServiceImp implements SystemAuthService {

	@Autowired
	SystemAuthDao systemAuthDao
	
	@Override
	void save(SystemAuth auth) {
		// TODO Auto-generated method stub
		systemAuthDao.save(auth)
	}

	@Override
	void batchDeleteByRoleId(String roleid) {
		// TODO Auto-generated method stub
		systemAuthDao.deleteInBatch(systemAuthDao.findByRoleid(roleid))
	}
}