package com.doosan.ddp.pm.service.imp.biz.pro
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.doosan.ddp.pm.dao.domain.biz.pro.ProgramInvoice
import com.doosan.ddp.pm.dao.jpa.biz.pro.ProgramInvoiceDao
import com.doosan.ddp.pm.service.biz.pro.ProgramInvoiceService
@Service
class ProgramInvoiceServiceImp implements ProgramInvoiceService {
	
	@Autowired
	ProgramInvoiceDao programInvoiceDao 

	@Transactional
	void save(ProgramInvoice invoice) {
		// TODO Auto-generated method stub
		programInvoiceDao.save(invoice)
	}

	@Transactional
	List<ProgramInvoice> getProgramInvoiceByProId(String proId) {
		// TODO Auto-generated method stub
		return programInvoiceDao.findByProgramid(proId)
	}

	@Transactional
	ProgramInvoice getById(String id) {
		// TODO Auto-generated method stub
		return programInvoiceDao.getOne(id)
	}
}