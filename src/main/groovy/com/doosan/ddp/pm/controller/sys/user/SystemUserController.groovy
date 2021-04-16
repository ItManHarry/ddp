package com.doosan.ddp.pm.controller.sys.user
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
import com.doosan.ddp.pm.dao.domain.sys.org.SystemOrg
import com.doosan.ddp.pm.dao.domain.sys.role.SystemRole
import com.doosan.ddp.pm.dao.domain.sys.user.SystemUser
import com.doosan.ddp.pm.service.sys.dict.SystemDictionaryService
import com.doosan.ddp.pm.service.sys.dict.SystemEnumerationService
import com.doosan.ddp.pm.service.sys.org.SystemOrgService
import com.doosan.ddp.pm.service.sys.role.SystemRoleService
import com.doosan.ddp.pm.service.sys.user.SystemUserService
import com.google.gson.JsonObject
import com.google.gson.JsonParser
@Controller
@RequestMapping("/pm/sys/user")
class SystemUserController {
	
	final String WEB_URL = "sys/user"
	@Autowired
	SystemUserService systemUserService
	@Autowired
	SystemRoleService systemRoleService
	@Autowired
	SystemDictionaryService systemDictionaryService
	@Autowired
	SystemEnumerationService systemEnumerationService
	@Autowired
	SystemOrgService systemOrgService
	
	/**
	 *	 跳转用户清单
	 * 	@return
	 */
	@RequestMapping("/list")
	def list(HttpServletRequest request, Map map) {
		if(systemUserService.getCount())
			map.put("total", systemUserService.getCount().intValue())
		else
			map.put("total", 0)
		return WEB_URL + "/list"
	}
	
	/**
	 * 	获取数据
	 * 	@param page
	 * 	@param limit
	 * 	@return
	 */
	@ResponseBody
	@GetMapping("/all")
	def all(Integer page, Integer limit){
		List<SystemRole> roles = systemRoleService.getAll()
		def roleMap = [:]
		roles.each {
			roleMap.put(it.getTid(), it.getRolename())
		}
		List<SystemOrg> orgs = systemOrgService.getAll()
		def orgMap = [:]
		orgs.each {
			orgMap.put(it.getTid(), it.getOrgname())
		}
		def types = systemEnumerationService.getByDictionary(systemDictionaryService.getDictByCode('D022').getTid())
		def typeMap = [:]
		types.each {
			typeMap.put(it.getValue(), it.getView())
		}
		typeMap.each { k,v ->
			println "Key is : $k, value is : $v"
		}
		def count = systemUserService.getCount() ? systemUserService.getCount().intValue() : 0
		def data = systemUserService.getAllByPages(page, limit)
		data.each {
			it.setUtStr(typeMap.get(it.getUsertype().toString()))
			it.setUrStr(roleMap.get(it.getUserrole()))
			it.setUoStr(orgMap.get(it.getUserorg()))
			if(it.getStatus() == 2)
				it.setStsStr("停用")
			else
				it.setStsStr("在用")
		}
		return ServerResultJson.success(data, count)
	}
	/**
	 * 根据姓名和code进行模糊分页查询
	 * @param page
	 * @param limit
	 * @param name
	 * @param code
	 * @return
	 */
	@ResponseBody
	@GetMapping("/query")
	def query(Integer page, Integer limit, String name, String code){
		List<SystemRole> roles = systemRoleService.getAll()
		def roleMap = [:]
		roles.each { 
			roleMap.put(it.getTid(), it.getRolename())			
		}
		List<SystemOrg> orgs = systemOrgService.getAll()
		def orgMap = [:]
		orgs.each { 
			orgMap.put(it.getTid(), it.getOrgname())
		}
		def types = systemEnumerationService.getByDictionary(systemDictionaryService.getDictByCode('D001').getTid())
		def typeMap = [:]
		types.each { 
			typeMap.put(it.getValue(), it.getView())
		}
		def count = systemUserService.getCountByNameAndCode(name, code) ? systemUserService.getCountByNameAndCode(name, code).intValue() : 0
		def data = systemUserService.findByNameAndCode(page, limit, name, code)
		data.each {
			it.setUtStr(typeMap.get(it.getUsertype().toString()))
			it.setUrStr(roleMap.get(it.getUserrole()))
			it.setUoStr(orgMap.get(it.getUserorg()))
			if(it.getStatus() == 2)
				it.setStsStr("停用")
			else
				it.setStsStr("在用")
		}
		return ServerResultJson.success(data, count)
	}
	/**
	 * 	保存用户
	 * 	@param request
	 * 	@param map
	 * 	@return
	 */
	@PostMapping("/save")
	@ResponseBody
	def save(@RequestBody String params, HttpServletRequest request, Map map){
		//session获取用户账号
		def userId = request.getSession().getAttribute("currentUser")
		//传递参数值
		println 'Parameters : \t' + params
		JsonObject json = JsonParser.parseString(params).getAsJsonObject()
		String id = json.get("id").asString
		String code = json.get("code").asString
		String name = json.get("name").asString
		String userrole = json.get("userrole").asString
		int usertype = json.get("usertype").asInt
		String userorg = json.get("userorg").asString
		int status = json.get("status").asInt
		String pwd = json.get("pwd").asString
		String email = json.get("email").asString
		String svncode = json.get("svncode").asString
		String svnpwd = json.get("svnpwd").asString
		//String userId = json.get("user").asString
		SystemUser user = new SystemUser()
		if(id) {
			user = systemUserService.getUserById(id);
			user.setModifytime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
			user.setModifyuserid(userId)
		}else {
			def d = systemUserService.getUserByCode(code.toUpperCase())
			if(d != null)
				return ServerResultJson.error(0, "用户已存在,请勿重复添加!", "")
			user.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
			user.setCreateuserid(userId)
		}
		user.setCode(code.toUpperCase())
		user.setName(name)
		user.setUserrole(userrole)
		user.setUsertype(usertype)
		user.setUserorg(userorg)
		user.setEmail(email)
		user.setStatus(status)
		user.setPwd(pwd)
		user.setSvncode(svncode)
		user.setSvnpwd(svnpwd)
		systemUserService.save(user)
		return ServerResultJson.success()
	}
	/**
	 * 	启用/停用用户
	 * 	@param request
	 * 	@param map
	 * 	@return
	 */
	@PostMapping("/status")
	@ResponseBody
	def status(@RequestBody String params, HttpServletRequest request, Map map){
		//session获取用户账号
		def userId = request.getSession().getAttribute("currentUser")
		//参数传递用户账号
		println 'Parameters : \t' + params
		JsonObject json = JsonParser.parseString(params).getAsJsonObject()
		String id = json.get("id").asString
		int status = json.get("status").asInt
		SystemUser user = systemUserService.getUserById(id)
		user.setModifytime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
		user.setModifyuserid(userId)
		user.setStatus(status)
		systemUserService.save(user)
		return ServerResultJson.success()
	}
}