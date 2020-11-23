package com.doosan.ddp.pm.service.sys.role
import com.doosan.ddp.pm.dao.domain.sys.role.SystemRole

interface SystemRoleService {	
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