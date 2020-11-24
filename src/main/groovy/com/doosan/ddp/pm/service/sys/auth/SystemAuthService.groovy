package com.doosan.ddp.pm.service.sys.auth
import com.doosan.ddp.pm.dao.domain.sys.auth.SystemAuth

interface SystemAuthService {
	
	//保存权限
	void save(SystemAuth auth) 
	//根据角色ID批量删除权限
	void batchDeleteByRoleId(String roleid)
	
}