package com.doosan.ddp.pm.dao.jpa.sys.auth
import org.springframework.data.jpa.repository.JpaRepository
import com.doosan.ddp.pm.dao.domain.sys.auth.SystemAuth

interface SystemAuthDao extends JpaRepository<SystemAuth, String> {

	List<SystemAuth> findByRoleid(String roleid)	
}