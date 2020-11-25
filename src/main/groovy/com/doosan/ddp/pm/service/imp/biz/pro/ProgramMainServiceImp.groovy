package com.doosan.ddp.pm.service.imp.biz.pro
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import com.doosan.ddp.pm.dao.domain.biz.pro.ProgramMain
import com.doosan.ddp.pm.dao.jpa.biz.pro.ProgramMainDao
import com.doosan.ddp.pm.service.biz.pro.ProgramMainService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
@Service
class ProgramMainServiceImp implements ProgramMainService {
	
	@Autowired
	ProgramMainDao programMainDao

	@Transactional
	long getCount() {
		// TODO Auto-generated method stub
		return programMainDao.count()
	}

	@Transactional
	List<ProgramMain> getAllByPages(Integer page, Integer limit) {
		// TODO Auto-generated method stub
		Sort sort = Sort.by("code","name")
		Pageable pageable = PageRequest.of(page-1, limit, sort)			//page从0开始
		Page<ProgramMain> pageData = programMainDao.findAll(pageable)
		return pageData.getContent()
	}

	@Transactional
	void save(ProgramMain program) {
		// TODO Auto-generated method stub
		programMainDao.save(program)
	}

	@Transactional
	ProgramMain getProgramByCode(String code) {
		// TODO Auto-generated method stub
		List<ProgramMain> programs = programMainDao.findByCode(code)
		if(programs != null && programs.size() != 0)
			return programs.get(0)
		else
			return null
	}

	@Transactional
	ProgramMain getProgramById(String id) {
		// TODO Auto-generated method stub
		return programMainDao.getOne(id)
	}

	@Transactional
	List<ProgramMain> findByNameAndCode(Integer page, Integer limit, String name, String code) {
		// TODO Auto-generated method stub
		Sort sort = Sort.by("code","name")
		Pageable pageable = PageRequest.of(page-1, limit, sort)			//page从0开始
		Page<ProgramMain> pageData = programMainDao.findAll(getSpec(name, code), pageable)
	}

	@Transactional
	long getCountByNameAndCode(String name, String code) {
		// TODO Auto-generated method stub
		return programMainDao.count(getSpec(name, code))
	}
	
	Specification<ProgramMain> getSpec(String name, String code) {
		// TODO Auto-generated method stub
		Specification<ProgramMain> spec = new Specification<ProgramMain>(){
			/**
			 * Root<ProgramMain>:根对象，用于查询对象的属性
			 *  CriteriaQuery<?>:执行普通查询
			 *  CriteriaBuilder:查询条件构造器,用于完成不同条件的查询
			 *
			 */
			public Predicate toPredicate(Root<ProgramMain> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				// where name like ? and gender like ?
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