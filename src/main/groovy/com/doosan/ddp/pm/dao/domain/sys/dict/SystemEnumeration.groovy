package com.doosan.ddp.pm.dao.domain.sys.dict
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table
import com.doosan.ddp.pm.dao.domain.base.TableEntityBaseModel
/**
 * 字典枚举值表
 */
@Entity
@Table(name="tb_enum")
class SystemEnumeration extends TableEntityBaseModel {
	//字典所属uuid
	@Column(name="dict", length=40)
	String dict
	//字典值
	@Column(name="value", length=20)
	String value
	//字典显示值
	@Column(name="view", length=40)
	String view	
}