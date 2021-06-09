package com.doosan.ddp.pm.controller.sys.menu
import java.text.SimpleDateFormat
import javax.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import com.doosan.ddp.pm.comm.results.ServerResultJson
import com.doosan.ddp.pm.dao.domain.sys.menu.SystemMenu
import com.doosan.ddp.pm.service.sys.auth.SystemAuthService
import com.doosan.ddp.pm.service.sys.menu.SystemMenuService
import com.google.gson.JsonObject
import com.google.gson.JsonParser
@Controller
@RequestMapping("/pm/sys/menu")
class SystemMenuController {
	
	final String WEB_URL = "sys/menu"
	@Autowired
	SystemMenuService systemMenuService
	@Autowired
	SystemAuthService systemAuthService
	
	/**
	 *	 跳转菜单清单
	 * 	@return
	 */
	@RequestMapping("/list")
	def list(HttpServletRequest request, Map map) {
		if(systemMenuService.getCount())
			map.put("total", systemMenuService.getCount().intValue())
		else
			map.put("total", 0)
		return WEB_URL + "/list"
	}
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
			if(it.getStatus() == 2)
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
	def save(@RequestBody String params, HttpServletRequest request, Map map){
		//session获取用户账号
		def user = request.getSession().getAttribute("currentUser")
		//传递参数值
		println 'Parameters : \t' + params
		JsonObject json = JsonParser.parseString(params).getAsJsonObject()
		String id = json.get("id").asString
		String menuname = json.get("menuname").asString
		String url = json.get("url").asString
		int status = json.get("status").asInt
		SystemMenu menu = new SystemMenu()
		if(id) {
			menu = systemMenuService.getMenuById(id);
			menu.setModifytime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
			menu.setModifyuserid(user)
		}else {
			menu.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
			menu.setCreateuserid(user)
		}
		menu.setStatus(status)
		menu.setUrl(url)
		menu.setMenuname(menuname)
		systemMenuService.save(menu)
		return ServerResultJson.success()
	}
	
	/**
	 * 	获取所有菜单数据
	 * 	@return
	 */
	@ResponseBody
	@GetMapping("/items")
	def items(String roleid){
		def menus = systemMenuService.getAll()
//		menus.each {
//			if(it.getStatus() == 2)
//				it.setStsStr("停用")
//			else
//				it.setStsStr("在用")
//		}
		def authed = systemAuthService.getByRoleId(roleid)
		def authedMenu = []
		authed.each{
			authedMenu << it.getMenuid()
		}
		def data = [:]
		data.put("menus", menus.findAll { it.getStatus() !=2  })	//所有在用菜单
		data.put("authed", authedMenu)								//已授权菜单
		return ServerResultJson.success(data)
	}
}