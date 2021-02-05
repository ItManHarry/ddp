package com.doosan.ddp.pm.service.sys.menu
import com.doosan.ddp.pm.dao.domain.sys.menu.SystemMenu

interface SystemMenuService {
	
	/**
	 * 保存菜单
	 * @param menu
	 */
	void save(SystemMenu menu)
	
	/**
	 *	 获取记录总数
	 * 	@return
	 */
	long getCount()
	/**
	 * 	分页获取系统菜单
	 * 	@param page
	 * 	@param limit
	 * 	@return
	 */
	List<SystemMenu> getAllByPages(Integer page, Integer limit)
	/**
	 * 根据id获取菜单
	 * @param id
	 * @return
	 */
	SystemMenu getMenuById(String id)
	/**
	 * 获取所有的菜单
	 * @return
	 */
	List<SystemMenu> getAll()
}