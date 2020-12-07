package com.doosan.ddp.pm.service.imp.sys.role
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import com.doosan.ddp.pm.dao.domain.sys.role.SystemRole
import com.doosan.ddp.pm.dao.jpa.sys.role.SystemRoleDao
import com.doosan.ddp.pm.service.sys.role.SystemRoleService
@Service
class SystemRoleServiceImp implements SystemRoleService {
	
	@Autowired
	SystemRoleDao systemRoleDao

	@Transactional
	public long getCount() {
		// TODO Auto-generated method stub
		return systemRoleDao.count()
	}
	
	@Transactional
	List<SystemRole> getAllByPages(Integer page, Integer limit) {
		// TODO Auto-generated method stub
		Sort sort = Sort.by("rolename")
		Pageable pageable = PageRequest.of(page-1, limit, sort)		//page从0开始
		Page<SystemRole> pageData = systemRoleDao.findAll(pageable)
		return pageData.getContent()
	}
	
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
