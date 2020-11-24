package com.doosan.ddp.pm.service.imp.sys.org
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.doosan.ddp.pm.dao.domain.sys.org.SystemOrg
import com.doosan.ddp.pm.dao.jpa.sys.org.SystemOrgDao
import com.doosan.ddp.pm.service.sys.org.SystemOrgService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
@Service
class SystemOrgServiceImp implements SystemOrgService {

	@Autowired
	SystemOrgDao systemOrgDao
	
	@Transactional
	long getCount() {
		// TODO Auto-generated method stub
		return systemOrgDao.count()
	}

	@Transactional
	List<SystemOrg> getAllByPages(Integer page, Integer limit) {
		// TODO Auto-generated method stub
		Sort sort = Sort.by("code","name")
		Pageable pageable = PageRequest.of(page-1, limit, sort)		//page从0开始
		Page<SystemOrg> pageData = systemOrgDao.findAll(pageable)
		return pageData.getContent()
	}

	@Transactional
	void save(SystemOrg org) {
		// TODO Auto-generated method stub
		systemOrgDao.save(org)
	}

	@Transactional
	SystemOrg getOrgById(String id) {
		// TODO Auto-generated method stub
		return systemOrgDao.getOne(id)
	}
}