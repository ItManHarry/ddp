package com.doosan.ddp.pm.dao.domain.biz.pro
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table
import com.doosan.ddp.pm.dao.domain.base.TableEntityBaseModel
/**
 * 项目状态信息
 */
@Entity
@Table(name="tb_program_status")
class ProgramStatus extends TableEntityBaseModel {
	//所属项目(项目uuid)
	@Column(name="programid", length=40)
	String programid
	//客户公司(存储组织uuid)
	@Column(name="company", length=40)
	String company
	//是否新项目(1 :  是 2 : 否)
	@Column(name="newpro")
	int newpro
	//项目分类(1：C&SI服务卖出 2：C&SI商品卖出)
	@Column(name="category")
	int category
	//项目状态(1：等待 2：合同准备 3：起案进行 4：进行中 5：结束)
	@Column(name="state")
	int state
	//项目执行可能性(0 - 100)%
	@Column(name="possible")
	int possible
	//合同开始日期
	@Column(name="contractstart", length=20)
	String contractstart
	//合同结束日期
	@Column(name="contractend", length=20)
	String contractend
	//法人(存储组织信息uuid，维护DICC/DISD/DIVC/DICI等法人信息)
	@Column(name="legalorg", length=40)
	String legalorg
	//客户公司主管部门
	@Column(name="legaldept", length=100)
	String legaldept
	//DDIC主管部门(存储组织uuid，维护ddic各个Team信息）
	@Column(name="ddicdept", length=40)
	String ddicdept
	//事业预算
	@Column(name="budget")
	double budget
	//进行情况
	@Column(name="process", length=40)
	String process
}