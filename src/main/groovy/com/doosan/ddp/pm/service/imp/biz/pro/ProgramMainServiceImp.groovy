package com.doosan.ddp.pm.service.imp.biz.pro
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional

import com.doosan.ddp.pm.dao.domain.biz.issue.ProgramIssue
import com.doosan.ddp.pm.dao.domain.biz.pro.ProgramMain
import com.doosan.ddp.pm.dao.jpa.biz.issue.ProgramIssueDao
import com.doosan.ddp.pm.dao.jpa.biz.pro.ProgramMainDao
import com.doosan.ddp.pm.service.biz.pro.ProgramMainService
import com.doosan.ddp.pm.service.imp.base.BaseServiceImpl
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
	List<ProgramMain> getByNameAndCodeForGroup(Integer page, Integer limit, String name, String code, List<String> tids) {
		// TODO Auto-generated method stub
		Sort sort = Sort.by("code","name")
		Pageable pageable = PageRequest.of(page-1, limit, sort)			//page从0开始
		Page<ProgramMain> pageData = programMainDao.findAll(getSpecForGroup(name, code, tids), pageable)
		return pageData.getContent()
	}

	@Transactional
	long getCountByNameAndCodeForGroup(String name, String code, List<String> tids) {
		// TODO Auto-generated method stub
		return programMainDao.count(getSpecForGroup(name, code, tids))
	}
	@Transactional
	List<ProgramMain> getByNameAndCodeForPM(Integer page, Integer limit, String name, String code, String charger) {
		// TODO Auto-generated method stub
		Sort sort = Sort.by("code","name")
		Pageable pageable = PageRequest.of(page-1, limit, sort)			//page从0开始
		Page<ProgramMain> pageData = programMainDao.findAll(getSpecForPM(name, code, charger), pageable)
		return pageData.getContent()
	}

	@Transactional
	long getCountByNameAndCodeForPM(String name, String code, String charger) {
		// TODO Auto-generated method stub
		return programMainDao.count(getSpecForPM(name, code, charger))
	}
	@Transactional
	List<ProgramMain> getProListForUser(List<String> ids) {
		// TODO Auto-generated method stub
		return programMainDao.findAll(getSpecForProList(ids))
	}
	
	@Transactional
	long getCountForReport(String name, String code, String conCd, String prCd) {
		// TODO Auto-generated method stub
		return programMainDao.count(getSpecForReport(name, code, conCd, prCd))
	}
	@Transactional
	List<ProgramMain> getProListForReport(Integer page, Integer limit, String name, String code, String conCd, String prCd) {
		// TODO Auto-generated method stub
		Sort sort = Sort.by("code","name")
		Pageable pageable = PageRequest.of(page-1, limit, sort)			//page从0开始
		Page<ProgramMain> pageData = programMainDao.findAll(getSpecForReport(name, code, conCd, prCd), pageable)
		return pageData.getContent()
	}
	@Transactional
	List<ProgramMain> getProListForReportAll(String name, String code, String conCd, String prCd) {
		return programMainDao.findAll(getSpecForReport(name, code, conCd, prCd))
	}
	@Transactional
	List<ProgramMain> getAll(){
		return programMainDao.findAll()
	}
	/**
	 * 获取所有参与的项目清单 - 项目中的每个人	 
	 * @param name
	 * @param code
	 * @param tids
	 * @return
	 */
	Specification<ProgramMain> getSpecForGroup(String name, String code, List<String> tids) {
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
	/**
	 * 获取所有参与的项目list
	 * @param tids
	 * @return
	 */
	Specification<ProgramMain> getSpecForProList(List<String> tids) {
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
	/**
	 * 获取PM的所有项目信息
	 * @param name
	 * @param code
	 * @param charger
	 * @return
	 */
	Specification<ProgramMain> getSpecForPM(String name, String code, String charger) {
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
				predicates.add(builder.equal(root.get("charger"), charger))
				Predicate[] predicateArray = new Predicate[predicates.size()]
				return builder.and(predicates.toArray(predicateArray))
			}
		}
		return spec
	}
	/**
	 * 获取报表所有项目信息
	 * @param name
	 * @param code
	 * @param conCd
	 * @param prCd
	 * @return
	 */
	Specification<ProgramMain> getSpecForReport(String name, String code, String conCd, String prCd) {
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
				predicates.add(builder.like(root.get("contractno"), "%"+conCd+"%"))
				predicates.add(builder.like(root.get("prno"), "%"+prCd+"%"))
				Predicate[] predicateArray = new Predicate[predicates.size()]
				return builder.and(predicates.toArray(predicateArray))
			}
		}
		return spec
	}
}