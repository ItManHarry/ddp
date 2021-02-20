package com.doosan.ddp.pm.dao.jpa.sys.menu
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import com.doosan.ddp.pm.dao.domain.sys.menu.SystemMenu

interface SystemMenuDao extends JpaRepository<SystemMenu, String>, JpaSpecificationExecutor<SystemMenu> {

}