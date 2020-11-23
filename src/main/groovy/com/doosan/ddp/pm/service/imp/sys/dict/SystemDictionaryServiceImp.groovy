package com.doosan.ddp.pm.service.imp.sys.dict
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.doosan.ddp.pm.dao.domain.sys.dict.SystemDictionary
import com.doosan.ddp.pm.dao.jpa.sys.dict.SystemDictionaryDao
import com.doosan.ddp.pm.service.sys.dict.SystemDictionaryService
@Service
class SystemDictionaryServiceImp implements SystemDictionaryService {
	
	@Autowired
	SystemDictionaryDao systemDictionaryDao

	@Transactional
	long getCount() {
		// TODO Auto-generated method stub
		return systemDictionaryDao.count()
	}

	@Transactional
	List<SystemDictionary> getAllByPages(Integer page, Integer limit) {
		// TODO Auto-generated method stub
		Sort sort = Sort.by("code","name")
		Pageable pageable = PageRequest.of(page-1, limit, sort)				//page从0开始
		Page<SystemDictionary> pageData = systemDictionaryDao.findAll(pageable)
		return pageData.getContent()	
	}

	@Transactional
	void save(SystemDictionary dict) {
		// TODO Auto-generated method stub
		systemDictionaryDao.save(dict)
	}

	@Transactional
	SystemDictionary getDictByCode(String code) {
		// TODO Auto-generated method stub
		List<SystemDictionary> dicts = systemDictionaryDao.findByCode(code)
		if(dicts == null || dicts.size() == 0)
			return null
		else
			return dicts.get(0)
	}

	@Transactional
	SystemDictionary getDictById(String id) {
		// TODO Auto-generated method stub
		return systemDictionaryDao.getOne(id)
	}
}