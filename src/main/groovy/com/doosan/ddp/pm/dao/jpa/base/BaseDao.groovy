package com.doosan.ddp.pm.dao.jpa.base

import com.doosan.ddp.pm.dao.domain.base.TableEntityBaseModel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface BaseDao<T extends TableEntityBaseModel,ID extends String> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

}
