package com.doosan.ddp.pm.controller.sys.org
import java.text.SimpleDateFormat
import javax.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import com.doosan.ddp.pm.comm.results.ServerResultJson
import com.doosan.ddp.pm.dao.domain.sys.org.SystemOrg
import com.doosan.ddp.pm.service.sys.org.SystemOrgService
import com.google.gson.JsonObject
import com.google.gson.JsonParser
@Controller
@RequestMapping("/pm/sys/org")
class SystemOrgController {
	
	final String WEB_URL = "sys/org"
	@Autowired
	SystemOrgService systemOrgService
	
	/**
	 * 	跳转组织清单
	 * 	@return
	 */
	@RequestMapping("/list")
	def list(HttpServletRequest request, Map map) {
		def total = systemOrgService.getCount()
		if(total)
			map.put("total", total)
		else
			map.put("total", 0)
		return WEB_URL + "/list"
	}
	
	/**
	 * 	获取组织数据
	 * 	@param page
	 * 	@param limit
	 * 	@return
	 */
	@ResponseBody
	@GetMapping("/all")
	def all(int page, int limit){
		def count = systemOrgService.getCount() ? systemOrgService.getCount().intValue() : 0
		println "Count is : $count"
		def data = systemOrgService.getAllByPages(page, limit)
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
		def roles = systemOrgService.getAll()
		def data = []
		println "Data is : $data"
		def role = null
		roles.each {
			role = [:]
			//只添加在用角色
			if(it.getStatus() == 1) {
				role.put('value', it.getTid())
				role.put('view', it.getOrgname())
				data << role
			}
		}
		return ServerResultJson.success(data)
	}
	
	/**
	 * 	保存组织信息
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
		String orgname = json.get("orgname").asString
		int status = json.get("status").asInt
		//String user = json.get("user").asString
		SystemOrg org = new SystemOrg()
		if(id) {
			org = systemOrgService.getOrgById(id)
			org.setModifytime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
			org.setModifyuserid(user)
		}else {
			org.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
			org.setCreateuserid(user)
		}
		org.setOrgname(orgname)
		org.setStatus(status)
		systemOrgService.save(org)
		return ServerResultJson.success()
	}
}