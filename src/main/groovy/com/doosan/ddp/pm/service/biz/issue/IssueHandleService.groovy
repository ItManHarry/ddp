package com.doosan.ddp.pm.service.biz.issue
import com.doosan.ddp.pm.dao.domain.biz.issue.IssueHandleRecord

interface IssueHandleService {
	/**
	 * 保存issue处理记录
	 * @param record
	 */
	void save(IssueHandleRecord record)
	/**
	 * 根据issue id获取更改履历
	 * @param issueid
	 * @return
	 */
	List<IssueHandleRecord> getRecordsByIssueid(String issueid)
}
