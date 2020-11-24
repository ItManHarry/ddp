package com.doosan.ddp.pm.service.sys.org
import com.doosan.ddp.pm.dao.domain.sys.org.SystemOrg

interface SystemOrgService {
	/**
	 *	 获取记录总数
	 * 	@return
	 */
	long getCount()
	/**
	 * 	分页获取组织数据
	 * 	@param page
	 * 	@param limit
	 * 	@return
	 */
	List<SystemOrg> getAllByPages(Integer page, Integer limit)
	/**
	 * 保存组织信息
	 * @param org
	 */
	void save(SystemOrg org)
	/**
	 * 根据id获取组织信息
	 * @param id
	 * @return
	 */
	SystemOrg getOrgById(String id)
}
