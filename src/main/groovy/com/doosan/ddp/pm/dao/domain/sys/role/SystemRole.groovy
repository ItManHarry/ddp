package com.doosan.ddp.pm.dao.domain.sys.role
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table
import com.doosan.ddp.pm.dao.domain.base.TableEntityBaseModel
/**
 * 系统角色
 */
@Entity
@Table(name="tb_role")
class SystemRole extends TableEntityBaseModel {
	//角色名称
	@Column(name="rolename",length=50)
	String rolename
}