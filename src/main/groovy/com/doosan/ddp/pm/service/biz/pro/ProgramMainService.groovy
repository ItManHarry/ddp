package com.doosan.ddp.pm.service.biz.pro
import com.doosan.ddp.pm.dao.domain.biz.pro.ProgramMain

interface ProgramMainService {
	/**
	 *	 获取记录总数
	 * 	@return
	 */
	long getCount()
	/**
	 * 	分页获取项目主数据
	 * 	@param page
	 * 	@param limit
	 * 	@return
	 */
	List<ProgramMain> getAllByPages(Integer page, Integer limit)
	/**
	 * 	新增/修改项目
	 * 	@param program
	 */
	void save(ProgramMain program)
	/**
	 * 	根据项目编号获取项目信息
	 * 	@param code
	 * 	@return
	 */
	ProgramMain getProgramByCode(String code)
	/**
	 * 	根据ID获取项目
	 * 	@param id
	 * 	@return
	 */
	ProgramMain getProgramById(String id)
	/**
	 * 根据项目名称和代码进行模糊分页查询 - 项目组人员
	 * @param page
	 * @param limit
	 * @param name
	 * @param code
	 * @return
	 */
	List<ProgramMain> getByNameAndCodeForGroup(Integer page, Integer limit, String name, String code, List<String> tids)
	/**
	 * 根据项目名称和代码进行模糊分页查询记录数 - 项目组人员
	 * @param name
	 * @param code
	 * @return
	 */
	long getCountByNameAndCodeForGroup(String name, String code, List<String> tids)
	/**
	 * 根据项目名称和代码进行模糊分页查询 - PM
	 * @param page
	 * @param limit
	 * @param name
	 * @param code
	 * @return
	 */
	List<ProgramMain> getByNameAndCodeForPM(Integer page, Integer limit, String name, String code, String charger)
	/**
	 * 根据项目名称和代码进行模糊分页查询记录数 - PM
	 * @param name
	 * @param code
	 * @return
	 */
	long getCountByNameAndCodeForPM(String name, String code, String charger)
	/**
	 * 获取所有参与的项目list
	 * @param ids
	 * @return
	 */
	List<ProgramMain> getProListForUser(List<String> ids)
	/**
	 * 获取所有的项目
	 * @return
	 */
	List<ProgramMain> getAll()
}