package com.doosan.ddp.pm.dao.domain.biz.issue
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table
import com.doosan.ddp.pm.dao.domain.base.TableEntityBaseModel
/**
 * 项目issue信息
 */
@Entity
@Table(name="tb_program_issue")
class ProgramIssue extends TableEntityBaseModel {
	//所属项目(项目uuid)
	@Column(name="programid", length=40)
	String programid
	//类型（1：Bug   2：改善）
	@Column(name="issuetype")
	int issuetype
	//紧急度（1：低   2：中   3：高）
	@Column(name="issuegrade")
	int issuegrade	
	//描述(存储富文本格式或者富文本路径)
	@Column(name="issueremark", length=500)
	String issueremark
	//处理人员(存储用户uuid)
	@Column(name="handler", length=40)
	String handler
	//处理状态（1：未开始， 2：进行中，3：已完成，4：已取消， 5：再处理）
	@Column(name="state")
	int state
	//issue处理开始日期
	@Column(name="startdate")
	Date startdate
	//issue处理完成日期
	@Column(name="finishdate")
	Date finishdate
}