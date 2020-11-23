package com.doosan.ddp.pm.dao.jpa.sys.dict
import org.springframework.data.jpa.repository.JpaRepository
import com.doosan.ddp.pm.dao.domain.sys.dict.SystemDictionary

interface SystemDictionaryDao extends JpaRepository<SystemDictionary, String> {
	/**
	 * 根据代码获取字典
	 * @param code
	 * @return
	 */
	List<SystemDictionary> findByCode(String code)
}