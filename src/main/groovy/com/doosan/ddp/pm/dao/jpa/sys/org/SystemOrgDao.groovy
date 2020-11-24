package com.doosan.ddp.pm.dao.jpa.sys.org
import org.springframework.data.jpa.repository.JpaRepository
import com.doosan.ddp.pm.dao.domain.sys.org.SystemOrg

interface SystemOrgDao extends JpaRepository<SystemOrg, String> {

}