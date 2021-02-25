package com.doosan.ddp.pm.dao.jpa.biz.issue
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import com.doosan.ddp.pm.dao.domain.biz.issue.ProgramIssue

interface ProgramIssueDao extends JpaRepository<ProgramIssue, String>, JpaSpecificationExecutor<ProgramIssue> {

	List<ProgramIssue> findByProgramid(String program)
	
}