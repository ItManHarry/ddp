package com.doosan.ddp.pm.service.imp.biz.pro
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.doosan.ddp.pm.dao.domain.biz.pro.ProgramGroup
import com.doosan.ddp.pm.dao.jpa.biz.pro.ProgramGroupDao
import com.doosan.ddp.pm.service.biz.pro.ProgramGroupService
@Service
class ProgramGroupServiceImp implements ProgramGroupService {
	
	@Autowired
	ProgramGroupDao programGroupDao
	
	@Transactional
	void save(ProgramGroup group) {
		// TODO Auto-generated method stub
		programGroupDao.save(group)
	}

	@Transactional
	List<ProgramGroup> getProgramGroupByProId(String proId) {
		// TODO Auto-generated method stub
		return programGroupDao.findByProgramid(proId)
	}

	@Transactional
	ProgramGroup getById(String id) {
		// TODO Auto-generated method stub
		return programGroupDao.getOne(id)
	}

	@Transactional
	public List<ProgramGroup> getProgramGroupByUserId(String userId) {
		// TODO Auto-generated method stub
		return programGroupDao.findByUserid(userId)
	}
}