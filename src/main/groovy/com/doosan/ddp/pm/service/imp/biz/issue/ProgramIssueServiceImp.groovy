package com.doosan.ddp.pm.service.imp.biz.issue
import com.doosan.ddp.pm.service.imp.base.BaseServiceImpl
import javax.annotation.PostConstruct
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.doosan.ddp.pm.dao.domain.biz.issue.ProgramIssue
import com.doosan.ddp.pm.dao.jpa.biz.issue.ProgramIssueDao
import com.doosan.ddp.pm.service.biz.issue.ProgramIssueService
@Service
class ProgramIssueServiceImp extends BaseServiceImpl<ProgramIssueDao,ProgramIssue> implements ProgramIssueService {

	@Autowired
	ProgramIssueDao programIssueDao
	@PostConstruct
	void setDao(){
		super.setBaseDao(programIssueDao);
	}

	@Transactional
	void save(ProgramIssue issue) {
		// TODO Auto-generated method stub
		programIssueDao.save(issue)
	}

	@Transactional
	List<ProgramIssue> getProgramIssuesById(String proId) {
		// TODO Auto-generated method stub
		return programIssueDao.findByProgramid(proId)
	}
	@Transactional
	ProgramIssue getIssueById(String id) {
		return programIssueDao.getOne(id)
	}
	@Transactional
	List<ProgramIssue> getProgramIssuesByPidAndStat(String proId, int state) {
		// TODO Auto-generated method stub
		return programIssueDao.findAll(getSpecForState(proId, state))
	}

	/**
	 * 获取PM的所有项目信息
	 * @param name
	 * @param code
	 * @param tid
	 * @return
	 */
	Specification<ProgramIssue> getSpecForState(String programId, int state) {
		// TODO Auto-generated method stub
		Specification<ProgramIssue> spec = new Specification<ProgramIssue>(){
			/**
			 * Root<ProgramIssue>:根对象，用于查询对象的属性
			 *  CriteriaQuery<?>:执行普通查询
			 *  CriteriaBuilder:查询条件构造器,用于完成不同条件的查询
			 *
			 */
			public Predicate toPredicate(Root<ProgramIssue> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				// where name like ? and gender like ?
				List<Predicate> predicates = new ArrayList<Predicate>()
				predicates.add(builder.equal(root.get("programid"), programId))
				predicates.add(builder.equal(root.get("state"), state))
				Predicate[] predicateArray = new Predicate[predicates.size()]
				return builder.and(predicates.toArray(predicateArray))
			}
		}
		return spec
	}
}