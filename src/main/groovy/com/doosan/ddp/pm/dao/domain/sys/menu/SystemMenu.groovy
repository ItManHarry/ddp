package com.doosan.ddp.pm.dao.domain.sys.menu
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table
import javax.persistence.Transient

import com.doosan.ddp.pm.dao.domain.base.TableEntityBaseModel
/**
 * 系统菜单
 */
@Entity
@Table(name="tb_menu")
class SystemMenu extends TableEntityBaseModel {
	//菜单名称
	@Column(name="menuname", length=100)
	String menuname
	//菜单地址
	@Column(name="url", length=50)
	String url
	//菜单状态,此栏位不会生成列
	@Transient
	String stsStr
}