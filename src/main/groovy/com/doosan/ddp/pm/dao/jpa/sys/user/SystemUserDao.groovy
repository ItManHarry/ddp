package com.doosan.ddp.pm.dao.jpa.sys.user
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import com.doosan.ddp.pm.dao.domain.sys.user.SystemUser

interface SystemUserDao extends JpaRepository<SystemUser, String>, JpaSpecificationExecutor<SystemUser> {
	/**
	 * 	根据code获取用户列表
	 * 	@param code
	 * 	@return
	 */
	List<SystemUser> findByCode(String code)
}