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
	//issue等级（1：低   2：中   3：高）
	@Column(name="issuegrade")
	int issuegrade	
	//描述(存储富文本格式或者富文本路径)
	@Column(name="issueremark", length=500)
	String issueremark
	//处理人员(存储用户code)
	@Column(name="handler", length=40)
	String handler
	//处理状态（1：待确认  2：处理中 3：处理完成 4：已取消 5 : 已关闭 6 ：Reopen）
	/**
	 *	状态逻辑如下：
	 *	新建issue后，状态为“待确认”，开发人员联系issue提出人员进行issue确认，
	 *	如果认可issue事项则更新issue状态为“处理中”，否则有issue提出人修改issue事项为“已取消”。
	 *	开发人员处理完成issue后，将issue状态更新为“处理完成”，issue提出人登录对应的系统进行验证，
	 *	验证通过后将issue状态更新为“已关闭”，否则更新为“Reopen”，开发人再次处理该issue
	 */
	@Column(name="state")
	int state
	//issue处理开始日期
	@Column(name="startdate", length=20)
	String startdate
	//issue处理完成日期
	@Column(name="finishdate", length=20)
	String finishdate
}