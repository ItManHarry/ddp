package com.doosan.ddp.pm.dao.domain.sys.logs
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
	//用户uuid
	@Column(name="userid", length=100)
	String userid
}