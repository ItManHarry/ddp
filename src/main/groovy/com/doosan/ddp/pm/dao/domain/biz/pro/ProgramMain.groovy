package com.doosan.ddp.pm.dao.domain.biz.pro
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table
import com.doosan.ddp.pm.dao.domain.base.TableEntityBaseModel
/**
 * 项目主信息
 */
@Entity
@Table(name="tb_program_main")
class ProgramMain extends TableEntityBaseModel {
	//项目名称
	@Column(name="name", length=100)
	String name
	//项目编码
	@Column(name="code", length=30)
	String code
	//项目描述
	@Column(name="remark", length=200)
	String remark
	//项目负责人(用户uuid)
	@Column(name="charger", length=40)
	String charger
	//项目开始日期
	@Column(name="startdate")
	Date startdate
	//项目实际开始日期
	@Column(name="realstartdate")
	Date realstartdate
	//项目结束日期
	@Column(name="enddate")
	Date enddate
	//项目实际结束日期
	@Column(name="realenddate")
	Date realenddate
	//项目金额
	@Column(name="amount")
	double amount
	//合同编号
	@Column(name="contractno", length=40)
	String contractno
	//PR编号
	@Column(name="prno", length=40)
	String prno
}