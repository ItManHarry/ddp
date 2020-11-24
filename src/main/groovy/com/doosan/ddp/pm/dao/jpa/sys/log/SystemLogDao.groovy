package com.doosan.ddp.pm.dao.jpa.sys.log
import org.springframework.data.jpa.repository.JpaRepository
import com.doosan.ddp.pm.dao.domain.sys.log.SystemLog

interface SystemLogDao extends JpaRepository<SystemLog, String> {

}