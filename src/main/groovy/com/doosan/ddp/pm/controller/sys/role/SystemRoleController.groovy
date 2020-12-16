package com.doosan.ddp.pm.controller.sys.role
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
import com.doosan.ddp.pm.dao.domain.sys.role.SystemRole
import com.doosan.ddp.pm.service.sys.role.SystemRoleService
import com.google.gson.JsonObject
import com.google.gson.JsonParser
@Controller
@RequestMapping("/pm/sys/role")
class SystemRoleController {
	
	final String WEB_URL = "sys/role"
	@Autowired
	SystemRoleService systemRoleService
	
	/**
	 * 	跳转角色清单
	 * 	@return
	 */
	@RequestMapping("/list")
	def list(HttpServletRequest request, Map map) {
		def total = systemRoleService.getCount()
		if(total)
			map.put("total", total)
		else
			map.put("total", 0)
		return WEB_URL + "/list"
	}
	
	/**
	 * 	分页获取角色数据
	 * 	@param page
	 * 	@param limit
	 * 	@return
	 */
	@ResponseBody
	@GetMapping("/all")
	def all(int page, int limit){
		def count = systemRoleService.getCount() ? systemRoleService.getCount().intValue() : 0
		println "Count is : $count"
		def data = systemRoleService.getAllByPages(page, limit)
		println "Data is : $data"
		data.each {
			if(it.getStatus() == 2)
				it.setStsStr("停用")
			else
				it.setStsStr("在用")
		}
		return ServerResultJson.success(data, count)
	}
	
	/**
	 * 	获取全部角色数据
	 * 	@return
	 */
	@ResponseBody
	@GetMapping("/items")
	def items(){
		def roles = systemRoleService.getAll()
		def data = []
		println "Data is : $data"
		def role = null
		roles.each {
			role = [:]
			//只添加在用角色
			if(it.getStatus() == 1) {
				role.put('value', it.getTid())
				role.put('view', it.getRolename())
				data << role	
			}
		}
		return ServerResultJson.success(data)
	}
	
	/**
	 * 	保存系统角色
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
		String rolename = json.get("rolename").asString
		int status = json.get("status").asInt
		//String user = json.get("user").asString
		SystemRole role = new SystemRole()
		if(id) {
			role = systemRoleService.getRoleById(id);
			role.setModifytime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
			role.setModifyuserid(user)
		}else {
			role.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
			role.setCreateuserid(user)
		}
		role.setRolename(rolename)
		role.setStatus(status)
		systemRoleService.save(role)
		return ServerResultJson.success()
	}
}