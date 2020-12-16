package com.doosan.ddp.pm.service.biz.pro
import com.doosan.ddp.pm.dao.domain.biz.pro.ProgramGroup

interface ProgramGroupService {
	/**
	 * 保存项目组人员
	 * @param group
	 */
	void save(ProgramGroup group)
	/**
	 * 根据项目id获取项目组信息
	 * @param proId
	 * @return
	 */
	List<ProgramGroup> getProgramGroupByProId(String proId)
	/**
	 * 根据项目id获取项目组信息
	 * @param proId
	 * @return
	 */
	List<ProgramGroup> getProgramGroupByUserId(String userId)
	/**
	 * 根据id获取项目组信息
	 * @param id
	 * @return
	 */
	ProgramGroup getById(String id)
}
