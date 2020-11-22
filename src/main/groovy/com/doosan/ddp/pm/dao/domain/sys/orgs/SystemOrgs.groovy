package com.doosan.ddp.pm.dao.domain.sys.orgs
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table
import com.doosan.ddp.pm.dao.domain.base.TableEntityBaseModel
/**
 * 组织信息
 */
@Entity
@Table(name="tb_orgs")
class SystemOrgs extends TableEntityBaseModel {
	//组织名称
	@Column(name="orgname", length=100)
	String orgname
}