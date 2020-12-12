package com.doosan.ddp.pm.service.imp.sys.user
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.doosan.ddp.pm.dao.domain.sys.user.SystemUser
import com.doosan.ddp.pm.dao.jpa.sys.user.SystemUserDao
import com.doosan.ddp.pm.service.sys.user.SystemUserService
@Service
class SystemUserServiceImp implements SystemUserService {
	
	@Autowired
	private SystemUserDao systemUserDao;

	@Transactional
	long getCount() {
		// TODO Auto-generated method stub
		return systemUserDao.count()
	}

	@Transactional
	List<SystemUser> getAllByPages(Integer page, Integer limit) {
		// TODO Auto-generated method stub
		Sort sort = Sort.by("code","name")
		Pageable pageable = PageRequest.of(page-1, limit, sort)//page从0开始
		Page<SystemUser> pageData = systemUserDao.findAll(pageable)
		return pageData.getContent()
	}
	
	@Transactional
	public List<SystemUser> getAll() {
		// TODO Auto-generated method stub
		return systemUserDao.findAll()
	}

	@Transactional
	void save(SystemUser user) {
		// TODO Auto-generated method stub
		systemUserDao.save(user)
	}

	@Transactional
	SystemUser getUserByCode(String code) {
		// TODO Auto-generated method stub
		List<SystemUser> users = systemUserDao.findByCode(code)
		if(users != null && users.size() != 0)
			return users.get(0)
		else
			return null
	}

	@Transactional
	SystemUser getUserById(String id) {
		// TODO Auto-generated method stub
		return systemUserDao.getOne(id);
	}

	@Transactional
	List<SystemUser> findByNameAndCode(Integer page, Integer limit, String name, String code) {
		// TODO Auto-generated method stub
		Sort sort = Sort.by("code","name")
		Pageable pageable = PageRequest.of(page-1, limit, sort)//page从0开始
		Page<SystemUser> pageData = systemUserDao.findAll(getSpec(name, code), pageable)
		return pageData.getContent()
	}

	@Transactional
	long getCountByNameAndCode(String name, String code) {
		// TODO Auto-generated method stub
		return systemUserDao.count(getSpec(name, code))
	}
	
	Specification<SystemUser> getSpec(String name, String code) {
		// TODO Auto-generated method stub
		Specification<SystemUser> spec = new Specification<SystemUser>(){
			/**
			 * Root<SystemUser>:根对象，用于查询对象的属性
			 *  CriteriaQuery<?>:执行普通查询
			 *  CriteriaBuilder:查询条件构造器,用于完成不同条件的查询
			 *
			 */
			public Predicate toPredicate(Root<SystemUser> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				// where name like ? and code like ?
				List<Predicate> predicates = new ArrayList<Predicate>()
				predicates.add(builder.like(root.get("name"), "%"+name+"%"))
				predicates.add(builder.like(root.get("code"), "%"+code+"%"))
				Predicate[] predicateArray = new Predicate[predicates.size()]
				return builder.and(predicates.toArray(predicateArray))
			}
		}
		return spec
	}
}