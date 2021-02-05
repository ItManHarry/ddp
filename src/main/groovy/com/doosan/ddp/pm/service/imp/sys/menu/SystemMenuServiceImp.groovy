package com.doosan.ddp.pm.service.imp.sys.menu
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import com.doosan.ddp.pm.dao.domain.sys.menu.SystemMenu
import com.doosan.ddp.pm.dao.jpa.sys.menu.SystemMenuDao
import com.doosan.ddp.pm.service.sys.menu.SystemMenuService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
@Service
class SystemMenuServiceImp implements SystemMenuService {
	
	@Autowired
	SystemMenuDao systemMenuDao

	@Override
	void save(SystemMenu menu) {
		// TODO Auto-generated method stub
		systemMenuDao.save(menu)
	}

	@Override
	long getCount() {
		// TODO Auto-generated method stub
		return systemMenuDao.count()
	}

	@Override
	List<SystemMenu> getAllByPages(Integer page, Integer limit) {
		// TODO Auto-generated method stub
		Sort sort = Sort.by("menuname")
		Pageable pageable = PageRequest.of(page-1, limit, sort)//page从0开始
		Page<SystemMenu> pageData = systemMenuDao.findAll(pageable)
		return pageData.getContent()
	}

	@Override
	SystemMenu getMenuById(String id) {
		// TODO Auto-generated method stub
		return systemMenuDao.getOne(id)
	}
	@Override
	List<SystemMenu> getAll(){
		return systemMenuDao.findAll()
	}
}