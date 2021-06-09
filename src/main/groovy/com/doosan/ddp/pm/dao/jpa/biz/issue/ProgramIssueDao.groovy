package com.doosan.ddp.pm.dao.jpa.biz.issue
import com.doosan.ddp.pm.dao.jpa.base.BaseDao
import com.doosan.ddp.pm.dao.domain.biz.issue.ProgramIssue

interface ProgramIssueDao extends BaseDao<ProgramIssue,String> {

	List<ProgramIssue> findByProgramid(String program)
	
}