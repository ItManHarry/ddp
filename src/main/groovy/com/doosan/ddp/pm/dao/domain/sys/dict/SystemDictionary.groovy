package com.doosan.ddp.pm.dao.domain.sys.dict
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table
import javax.persistence.Transient

import com.doosan.ddp.pm.dao.domain.base.TableEntityBaseModel
/**
 * 系统字典表
 */
@Entity
@Table(name="tb_dict")
class SystemDictionary extends TableEntityBaseModel {
	//字典代码
	@Column(name="code",length=50)
	String code
	//字典名称
	@Column(name="name",length=50)
	String name
	//字典状态,此栏位不会生成列
	@Transient
	String stsStr;
}