package com.doosan.ddp.pm.dao.jpa.biz.pro
import org.springframework.data.jpa.repository.JpaRepository
import com.doosan.ddp.pm.dao.domain.biz.pro.ProgramStatus

interface ProgramStatusDao extends JpaRepository<ProgramStatus, String> {

	List<ProgramStatus> findByProgramid(String proId)
}