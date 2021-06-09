package com.doosan.ddp.pm.dao.jpa.biz.pro
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import com.doosan.ddp.pm.dao.domain.biz.pro.ProgramMain
import com.doosan.ddp.pm.dao.jpa.base.BaseDao

interface ProgramMainDao extends JpaRepository<ProgramMain, String>, JpaSpecificationExecutor<ProgramMain>,BaseDao<ProgramMain, String> {

	/**
	 * 根据项目代码获取项目
	 * @param code
	 * @return
	 */
	List<ProgramMain> findByCode(String code)
	
}