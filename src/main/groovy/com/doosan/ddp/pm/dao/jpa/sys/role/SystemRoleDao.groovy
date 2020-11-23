package com.doosan.ddp.pm.dao.jpa.sys.role
import org.springframework.data.jpa.repository.JpaRepository
import com.doosan.ddp.pm.dao.domain.sys.role.SystemRole

interface SystemRoleDao extends JpaRepository<SystemRole, String> {
	
}