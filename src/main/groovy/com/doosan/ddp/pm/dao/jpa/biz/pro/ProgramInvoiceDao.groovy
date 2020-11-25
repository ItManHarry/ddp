package com.doosan.ddp.pm.dao.jpa.biz.pro
import org.springframework.data.jpa.repository.JpaRepository
import com.doosan.ddp.pm.dao.domain.biz.pro.ProgramInvoice

interface ProgramInvoiceDao extends JpaRepository<ProgramInvoice, String> {
	
	List<ProgramInvoice> findByProgramid(String proId)
}