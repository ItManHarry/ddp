package com.doosan.ddp.pm.controller.sys.menu
import java.text.SimpleDateFormat
import javax.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import com.doosan.ddp.pm.comm.results.ServerResultJson
import com.doosan.ddp.pm.dao.domain.sys.menu.SystemMenu
import com.doosan.ddp.pm.service.sys.menu.SystemMenuService
@Controller
@RequestMapping("/pm/sys/menu")
class SystemMenuController {
	
	@Autowired
	SystemMenuService systemMenuService
	
	/**
	 * 	分页获取菜单数据
	 * 	@param page
	 * 	@param limit
	 * 	@return
	 */
	@ResponseBody
	@GetMapping("/all")
	def all(Integer page, Integer limit){
		def count = systemMenuService.getCount() ? systemMenuService.getCount().intValue() : 0
		def data = systemMenuService.getAllByPages(page, limit)
		data.each {
			if(it.getStatus() == 0)
				it.setStsStr("停用")
			else
				it.setStsStr("在用")
		}
		return ServerResultJson.success(data, count)
	}
	
	/**
	 * 	保存菜单
	 * 	@param request
	 * 	@param map
	 * 	@return
	 */
	@PostMapping("/save")
	@ResponseBody
	def save(HttpServletRequest request, Map map){
		//session获取用户账号
		//def userId = request.getSession().getAttribute("currentUser")
		//参数传递用户账号
		def userId = request.getParameter("userId")
		SystemMenu menu = new SystemMenu()
		String id = request.getParameter("id")
		if(id) {
			menu = systemMenuService.getMenuById(id);
			menu.setModifytime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
			menu.setModifyuserid(userId)
		}else {
			menu.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
			menu.setCreateuserid(userId)
		}
		menu.setStatus(1)
		menu.setUrl(request.getParameter("url"))
		menu.setMenuname(request.getParameter("menuname"))
		systemMenuService.save(menu)
		return ServerResultJson.success()
	}
}