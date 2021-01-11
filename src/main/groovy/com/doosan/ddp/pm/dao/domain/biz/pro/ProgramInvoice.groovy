package com.doosan.ddp.pm.dao.domain.biz.pro
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table
import javax.persistence.Transient

import com.doosan.ddp.pm.dao.domain.base.TableEntityBaseModel
/**
 * 项目发票信息
 */
@Entity
@Table(name="tb_program_invoice")
class ProgramInvoice extends TableEntityBaseModel {
	//所属项目(项目uuid)
	@Column(name="programid", length=40)
	String programid
	//区分代码（1：首付款 2：中期款 3：尾款）
	@Column(name="stage")
	int stage
	//区分显示
	@Transient
	String stageStr
	//支付比例
	@Column(name="percent")
	int percent
	//开票时间(YYYYMM)
	@Column(name="invoicedt", length=10)
	String invoicedt
}