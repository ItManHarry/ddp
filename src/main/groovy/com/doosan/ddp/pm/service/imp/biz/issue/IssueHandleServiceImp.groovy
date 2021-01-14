package com.doosan.ddp.pm.service.imp.biz.issue
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.doosan.ddp.pm.dao.domain.biz.issue.IssueHandleRecord
import com.doosan.ddp.pm.dao.jpa.biz.issue.IssueHandleDao
import com.doosan.ddp.pm.service.biz.issue.IssueHandleService
@Service
class IssueHandleServiceImp implements IssueHandleService {
	
	@Autowired
	IssueHandleDao issueHandleDao

	@Transactional
	public void save(IssueHandleRecord record) {
		// TODO Auto-generated method stub
		issueHandleDao.save(record)
	}

	@Transactional
	public List<IssueHandleRecord> getRecordsByIssueid(String issueid) {
		// TODO Auto-generated method stub
		return issueHandleDao.findByIssueid(issueid)
	}
}