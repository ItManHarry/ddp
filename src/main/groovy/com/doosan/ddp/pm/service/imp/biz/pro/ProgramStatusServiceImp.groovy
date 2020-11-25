package com.doosan.ddp.pm.service.imp.biz.pro
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.doosan.ddp.pm.dao.domain.biz.pro.ProgramStatus
import com.doosan.ddp.pm.dao.jpa.biz.pro.ProgramStatusDao
import com.doosan.ddp.pm.service.biz.pro.ProgramStatusService
@Service
class ProgramStatusServiceImp implements ProgramStatusService {
	
	@Autowired
	ProgramStatusDao programStatusDao

	@Transactional
	void save(ProgramStatus status) {
		// TODO Auto-generated method stub
		programStatusDao.save(status)
	}

	@Transactional
	List<ProgramStatus> getProgramStatusByProId(String proId) {
		// TODO Auto-generated method stub
		return programStatusDao.findByProgramid(proId)
	}

	@Transactional
	ProgramStatus getById(String id) {
		// TODO Auto-generated method stub
		return programStatusDao.getOne(id)
	}
}
