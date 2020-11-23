package com.doosan.ddp.pm.service.sys.dict
import com.doosan.ddp.pm.dao.domain.sys.dict.SystemDictionary
/**
 * 	系统字典
 */
public interface SystemDictionaryService {

	/**
	 * 	获取记录总数
	 * 	@return
	 */
	long getCount()
	/**
	 * 	分页获取字典数据
	 * 	@param page
	 * 	@param limit
	 * 	@return
	 */
	List<SystemDictionary> getAllByPages(Integer page, Integer limit)
	/**
	 * 	新增修改下拉字典
	 * 	@param dict
	 */
	void save(SystemDictionary dict)
	/**
	 * 	根据code获取字典
	 * 	@param code
	 * 	@return
	 */
	SystemDictionary getDictByCode(String code)
	/**
	 * 	根据ID获取字典数据
	 * 	@param id
	 * 	@return
	 */
	SystemDictionary getDictById(String id)
}