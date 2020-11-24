package com.doosan.ddp.pm.dao.domain.sys.log
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table
import com.doosan.ddp.pm.dao.domain.base.TableEntityBaseModel
/**
 * 系统日志
 */
@Entity
@Table(name="tb_log")
class SystemLog extends TableEntityBaseModel {
	//操作用户账号
	@Column(name="userid", length=40)
	String userid
	//操作内容
	@Column(name="content", length=200)
	String content
}