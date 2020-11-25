package com.doosan.ddp.pm.dao.jpa.biz.pro
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import com.doosan.ddp.pm.dao.domain.biz.pro.ProgramMain

interface ProgramMainDao extends JpaRepository<ProgramMain, String>, JpaSpecificationExecutor<ProgramMain> {

	/**
	 * 根据项目代码获取项目
	 * @param code
	 * @return
	 */
	List<ProgramMain> findByCode(String code)
	
}