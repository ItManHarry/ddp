package com.doosan.ddp.pm.service.sys.role
import com.doosan.ddp.pm.dao.domain.sys.role.SystemRole

interface SystemRoleService {	
	/**
	 *	 获取记录总数
	 * 	@return
	 */
	long getCount()
	/**
	 * 	分页获取角色数据
	 * 	@param page
	 * 	@param limit
	 * 	@return
	 */
	List<SystemRole> getAllByPages(Integer page, Integer limit)
	/**
	 * 获取所有的系统角色数据
	 * @return
	 */
	List<SystemRole> getAll()
	/**
	 * 	新增/修改角色
	 * 	@param user
	 */
	void save(SystemRole role)
	/**
	 * 	根据ID获取角色
	 * 	@param id
	 * 	@return
	 */
	SystemRole getRoleById(String id)
}