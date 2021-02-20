package com.doosan.ddp.pm.service.imp.sys.menu
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.doosan.ddp.pm.dao.domain.sys.menu.SystemMenu
import com.doosan.ddp.pm.dao.jpa.sys.menu.SystemMenuDao
import com.doosan.ddp.pm.service.sys.menu.SystemMenuService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
@Service
class SystemMenuServiceImp implements SystemMenuService {
	
	@Autowired
	SystemMenuDao systemMenuDao

	@Transactional
	void save(SystemMenu menu) {
		// TODO Auto-generated method stub
		systemMenuDao.save(menu)
	}

	@Transactional
	long getCount() {
		// TODO Auto-generated method stub
		return systemMenuDao.count()
	}

	@Transactional
	List<SystemMenu> getAllByPages(Integer page, Integer limit) {
		// TODO Auto-generated method stub
		Sort sort = Sort.by("menuname")
		Pageable pageable = PageRequest.of(page-1, limit, sort)//page从0开始
		Page<SystemMenu> pageData = systemMenuDao.findAll(pageable)
		return pageData.getContent()
	}

	@Transactional
	SystemMenu getMenuById(String id) {
		// TODO Auto-generated method stub
		return systemMenuDao.getOne(id)
	}
	@Transactional
	List<SystemMenu> getAll(){
		return systemMenuDao.findAll()
	}
	@Transactional
	List<SystemMenu> getMenusByIds(List<String> ids) {
		// TODO Auto-generated method stub
		return systemMenuDao.findAll(getSpecForMenuList(ids))
	}
	
	/**
	 * 获取ids获取菜单
	 * @param tids
	 * @return
	 */
	Specification<SystemMenu> getSpecForMenuList(List<String> tids) {
		// TODO Auto-generated method stub
		Specification<SystemMenu> spec = new Specification<SystemMenu>(){
			/**
			 * Root<SystemMenu>:根对象，用于查询对象的属性
			 *  CriteriaQuery<?>:执行普通查询
			 *  CriteriaBuilder:查询条件构造器,用于完成不同条件的查询
			 *
			 */
			public Predicate toPredicate(Root<SystemMenu> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				// where name like ? and gender like ?
				List<Predicate> predicates = new ArrayList<Predicate>()
				CriteriaBuilder.In<String> ins = builder.in(root.get("tid"))
				tids.each {
					ins.value(it)
				}
				predicates.add(ins)
				Predicate[] predicateArray = new Predicate[predicates.size()]
				return builder.and(predicates.toArray(predicateArray))
			}
		}
		return spec
	}
}