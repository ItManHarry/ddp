package com.doosan.ddp.pm.dao.jpa.biz.pro
import org.springframework.data.jpa.repository.JpaRepository
import com.doosan.ddp.pm.dao.domain.biz.pro.ProgramGroup

interface ProgramGroupDao extends JpaRepository<ProgramGroup, String> {

	List<ProgramGroup> findByProgramid(String proId)
	
	List<ProgramGroup> findByUserid(String userId)
	
}