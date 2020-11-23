package com.doosan.ddp.pm.dao.jpa.sys.dict
import org.springframework.data.jpa.repository.JpaRepository
import com.doosan.ddp.pm.dao.domain.sys.dict.SystemEnumeration

interface SystemEnumerationDao extends JpaRepository<SystemEnumeration, String> {
	/**
	 * 	根据字典ID获取枚举值
	 * 	@param dict
	 * 	@return
	 */
	List<SystemEnumeration> findByDict(String dict);
}