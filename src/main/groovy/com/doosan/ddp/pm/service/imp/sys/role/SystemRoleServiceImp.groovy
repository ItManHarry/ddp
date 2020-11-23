package com.doosan.ddp.pm.service.imp.sys.role
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.doosan.ddp.pm.dao.domain.sys.role.SystemRole
import com.doosan.ddp.pm.dao.jpa.sys.role.SystemRoleDao
import com.doosan.ddp.pm.service.sys.role.SystemRoleService
@Service
class SystemRoleServiceImp implements SystemRoleService {
	
	@Autowired
	SystemRoleDao systemRoleDao

	@Transactional
	void save(SystemRole role) {
		// TODO Auto-generated method stub
		systemRoleDao.save(role)
	}

	@Transactional
	SystemRole getRoleById(String id) {
		// TODO Auto-generated method stub
		return systemRoleDao.getOne(id)
	}
}
