package com.doosan.ddp.pm.service.imp.sys.dict
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.doosan.ddp.pm.dao.domain.sys.dict.SystemEnumeration
import com.doosan.ddp.pm.dao.jpa.sys.dict.SystemEnumerationDao
import com.doosan.ddp.pm.service.sys.dict.SystemEnumerationService
@Service
class SystemEnumerationServiceImp implements SystemEnumerationService {

	@Autowired
	SystemEnumerationDao systemEnumerationDao
	
	@Transactional
	void save(SystemEnumeration em) {
		// TODO Auto-generated method stub
		systemEnumerationDao.save(em)
	}

	@Transactional
	SystemEnumeration getEnumerationById(String id) {
		// TODO Auto-generated method stub
		return systemEnumerationDao.getOne(id)
	}

	@Transactional
	List<SystemEnumeration> findByDictionary(String dict) {
		// TODO Auto-generated method stub
		return systemEnumerationDao.findByDict(dict)
	}
}