package com.doosan.ddp.pm.dao.domain.biz.pro
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table
import javax.persistence.Transient

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
	//项目编码 - 生成规则PRO+YYYYMMDD+随机数
	@Column(name="code", length=30)
	String code
	//项目描述
	@Column(name="remark", length=200)
	String remark
	//项目负责人(用户代码)
	@Column(name="charger", length=40)
	String charger
	@Transient
	String chargerNm
	//项目开始日期
	@Column(name="startdate", length=20)
	String startdate
	//项目结束日期
	@Column(name="enddate", length=20)
	String enddate
	//项目金额
	@Column(name="amount")
	double amount
	//合同编号
	@Column(name="contractno", length=40)
	String contractno
	//PR编号
	@Column(name="prno", length=40)
	String prno
	//SVN地址
	@Column(name="svnadd", length=200)
	String svnadd
	@Transient
	ProgramStatus proStatus
}