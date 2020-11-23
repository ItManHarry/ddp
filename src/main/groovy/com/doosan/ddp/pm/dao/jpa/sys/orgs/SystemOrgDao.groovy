package com.doosan.ddp.pm.dao.jpa.sys.orgs
import org.springframework.data.jpa.repository.JpaRepository
import com.doosan.ddp.pm.dao.domain.sys.orgs.SystemOrg

interface SystemOrgDao extends JpaRepository<SystemOrg, String> {

}