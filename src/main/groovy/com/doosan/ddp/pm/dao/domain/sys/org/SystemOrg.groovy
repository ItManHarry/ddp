package com.doosan.ddp.pm.dao.domain.sys.org
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table
import javax.persistence.Transient

import com.doosan.ddp.pm.dao.domain.base.TableEntityBaseModel
/**
 * 组织信息
 */
@Entity
@Table(name="tb_org")
class SystemOrg extends TableEntityBaseModel {
	//组织名称
	@Column(name="orgname", length=100)
	String orgname
	//组织状态,此栏位不会生成列
	@Transient
	String stsStr;
}