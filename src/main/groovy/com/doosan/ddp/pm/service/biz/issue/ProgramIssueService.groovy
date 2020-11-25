package com.doosan.ddp.pm.service.biz.issue
import com.doosan.ddp.pm.dao.domain.biz.issue.ProgramIssue

interface ProgramIssueService {
	
	void save(ProgramIssue issue)
	
	List<ProgramIssue> getProgramIssues(String program)
	
	ProgramIssue getIssueById(String id)
	
}