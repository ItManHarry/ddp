package com.doosan.ddp.pm.dao.jpa.biz.issue
import org.springframework.data.jpa.repository.JpaRepository
import com.doosan.ddp.pm.dao.domain.biz.issue.IssueHandleRecord

interface IssueHandleDao extends JpaRepository<IssueHandleRecord, String> {
	
	List<IssueHandleRecord> findByIssueid(String issueid)
}