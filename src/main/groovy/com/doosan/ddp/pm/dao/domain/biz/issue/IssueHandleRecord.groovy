package com.doosan.ddp.pm.dao.domain.biz.issue
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table
import com.doosan.ddp.pm.dao.domain.base.TableEntityBaseModel
/**
 * issue处理履历
 */
@Entity
@Table(name="Tb_Issue_Handle")
class IssueHandleRecord extends TableEntityBaseModel {
	
	//issue ID(issue uuid)
	@Column(name="issueid", length=40)
	String issueid
	//issue状态
	@Column(name="state", length=40)
	String state
}
