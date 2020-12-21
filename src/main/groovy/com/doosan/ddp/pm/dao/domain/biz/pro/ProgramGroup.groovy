package com.doosan.ddp.pm.dao.domain.biz.pro
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table
import com.doosan.ddp.pm.dao.domain.base.TableEntityBaseModel
/**
 * 项目组人员信息
 * 对应User中的类型
 */
@Entity
@Table(name="tb_program_group")
class ProgramGroup extends TableEntityBaseModel {
	//所属项目(项目uuid)
	@Column(name="programid", length=40)
	String programid
	//项目成员(用户code)
	@Column(name="userid", length=40)
	String userid
	//项目组角色(1:PM 2:普通成员)
	@Column(name="grouprole")
	int grouprole
}