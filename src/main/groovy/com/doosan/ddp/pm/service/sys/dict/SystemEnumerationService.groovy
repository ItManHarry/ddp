package com.doosan.ddp.pm.service.sys.dict
import com.doosan.ddp.pm.dao.domain.sys.dict.SystemEnumeration
/**
 * 	字典枚举
 */
public interface SystemEnumerationService {
	/**
	 * 	新增/修改枚举
	 * 	@param em
	 */
	void save(SystemEnumeration em)
	/**
	 * 	根据ID获取枚举数据
	 * 	@param id
	 * 	@return
	 */
	SystemEnumeration getEnumerationById(String id)
	/**
	 * 	根据字典ID获取枚举值
	 * 	@param dict
	 * 	@return
	 */
	List<SystemEnumeration> getByDictionary(String dict)
}