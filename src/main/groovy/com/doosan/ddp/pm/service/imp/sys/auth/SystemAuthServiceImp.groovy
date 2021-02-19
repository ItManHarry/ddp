package com.doosan.ddp.pm.service.imp.sys.auth
import org.springframework.beans.factory.annotation.Autowired
import com.doosan.ddp.pm.service.sys.auth.SystemAuthService 
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.doosan.ddp.pm.dao.domain.sys.auth.SystemAuth
import com.doosan.ddp.pm.dao.jpa.sys.auth.SystemAuthDao
@Service
class SystemAuthServiceImp implements SystemAuthService {

	@Autowired
	SystemAuthDao systemAuthDao
	
	@Transactional
	void save(SystemAuth auth) {
		// TODO Auto-generated method stub
		systemAuthDao.save(auth)
	}

	@Transactional
	void batchDeleteByRoleId(String roleid) {
		// TODO Auto-generated method stub
		systemAuthDao.deleteInBatch(systemAuthDao.findByRoleid(roleid))
	}
	@Transactional
	List<SystemAuth> getByRoleId(String roleid){
		systemAuthDao.findByRoleid(roleid)
	}
}