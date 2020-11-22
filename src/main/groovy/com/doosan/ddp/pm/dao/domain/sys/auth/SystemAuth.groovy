package com.doosan.ddp.pm.dao.domain.sys.auth
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table
import com.doosan.ddp.pm.dao.domain.base.TableEntityBaseModel
/**
 *系统权限表
 */
@Entity
@Table(name="tb_auth")
class SystemAuth extends TableEntityBaseModel {
	//角色uuid
	@Column(name="roleid", length=40)
	String roleid
	//菜单uuid
	@Column(name="menuid", length=40)
	String menuid
}