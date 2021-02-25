package com.doosan.ddp.pm.service.biz.issue
import com.doosan.ddp.pm.dao.domain.biz.issue.ProgramIssue

interface ProgramIssueService {
	/**
	 * 保存issue信息
	 * @param issue
	 */
	void save(ProgramIssue issue)
	/**
	 * 根据项目id获取issue事项
	 * @param program
	 * @return
	 */
	List<ProgramIssue> getProgramIssuesById(String proId)
	/**
	 * 根据id获取issue
	 * @param id
	 * @return
	 */
	ProgramIssue getIssueById(String id)
	/**
	 * 根据项目id和项目状态获取issue数据
	 * @param proId
	 * @param state
	 * @return
	 */
	List<ProgramIssue> getProgramIssuesByPidAndStat(String proId, int state)
	
}