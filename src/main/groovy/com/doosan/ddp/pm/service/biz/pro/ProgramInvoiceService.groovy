package com.doosan.ddp.pm.service.biz.pro
import com.doosan.ddp.pm.dao.domain.biz.pro.ProgramInvoice

interface ProgramInvoiceService {
	/**
	 * 保存发票信息
	 * @param invoice
	 */
	void save(ProgramInvoice invoice)
	/**
	 * 根据项目id获取发票信息
	 * @param proId
	 * @return
	 */
	List<ProgramInvoice> getProgramInvoiceByProId(String proId)
	/**
	 * 根据id获取发票信息
	 * @param id
	 * @return
	 */
	ProgramInvoice getById(String id)
}