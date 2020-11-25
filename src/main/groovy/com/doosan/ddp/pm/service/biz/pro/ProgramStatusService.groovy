package com.doosan.ddp.pm.service.biz.pro

import com.doosan.ddp.pm.dao.domain.biz.pro.ProgramStatus

interface ProgramStatusService {
	/**
	 * 保存项目状态信息
	 * @param status
	 */
	void save(ProgramStatus status)
	/**
	 * 根据项目id获取项目状态信息
	 * @param proId
	 * @return
	 */
	List<ProgramStatus> getProgramStatusByProId(String proId)
	/**
	 * 根据id获取项目状态信息
	 * @param id
	 * @return
	 */
	ProgramStatus getById(String id)
}