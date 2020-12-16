package com.doosan.ddp.pm.service.imp.biz.issue
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import com.doosan.ddp.pm.dao.domain.biz.issue.ProgramIssue
import com.doosan.ddp.pm.dao.jpa.biz.issue.ProgramIssueDao
import com.doosan.ddp.pm.service.biz.issue.ProgramIssueService

class ProgramIssueServiceImp implements ProgramIssueService {

	@Autowired
	ProgramIssueDao programIssueDao
	
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
}