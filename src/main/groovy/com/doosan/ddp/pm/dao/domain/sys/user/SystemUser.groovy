package com.doosan.ddp.pm.dao.domain.sys.user
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table
import javax.persistence.Transient
import com.doosan.ddp.pm.dao.domain.base.TableEntityBaseModel
/**
 * 系统用户表
 */
@Entity
@Table(name="tb_user")
class SystemUser extends TableEntityBaseModel {
	//用户账号
	@Column(name="code",length=50)
	String code
	//用户姓名
	@Column(name="name",length=50)
	String name
	//用户密码(非工厂用户维护密码)
	@Column(name="pwd",length=50)
	String pwd
	//用户角色
	@Column(name="userrole",length=40)
	String userrole
	//用户类型（1：工厂 2：开发公司）
	@Column(name="usertype")
	int usertype
	//用户组织所属
	@Column(name="userorg",length=40)
	String userorg
	//用户状态,此栏位不会生成列
	@Transient
	String stsStr;
}