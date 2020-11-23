package com.doosan.ddp.pm.controller.sys.user
import javax.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import com.doosan.ddp.pm.comm.results.ServerResultJson
import com.doosan.ddp.pm.dao.domain.sys.user.SystemUser
import com.doosan.ddp.pm.service.sys.user.SystemUserService
@Controller
@RequestMapping("/pm/sys/user")
class SystemUserController {
	
	final String WEB_URL = "sys/users"
	@Autowired
	SystemUserService systemUserService
	
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
	 * 	@param order
	 * 	@param page
	 * 	@param limit
	 * 	@return
	 */
	@ResponseBody
	@GetMapping("/all")
	def all(Integer page, Integer limit){
		def count = systemUserService.getCount() ? systemUserService.getCount().intValue() : 0
		def data = systemUserService.getAllByPages(page, limit)
		data.each {
			if(it.getStatus() == 0)
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
		def count = systemUserService.getCountByNameAndCode(name, code) ? systemUserService.getCountByNameAndCode(name, code).intValue() : 0
		def data = systemUserService.findByNameAndCode(page, limit, name, code)
		data.each {
			if(it.getStatus() == 0)
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
	def save(HttpServletRequest request, Map map){
		def userId = request.getSession().getAttribute("currentUser")
		SystemUser user = new SystemUser()
		String tid = request.getParameter("tid")
		if(tid) {
			user = systemUserService.getUserById(tid);
			user.setModifytime(new Date())
			user.setModifyuserid(userId)
		}else {
			def d = systemUserService.getUserByCode(request.getParameter("code").toUpperCase())
			if(d != null)
				return ServerResultJson.error(0, "用户已存在,请勿重复添加!", "")
			user.setCreatetime(new Date())
			user.setCreateuserid(userId)
		}
		user.setCode(request.getParameter("code").toUpperCase())
		user.setName(request.getParameter("name"))
		user.setUserrole(request.getParameter("userrole"))
		user.setUsertype(Integer.parseInt(request.getParameter("usertype")))
		user.setUserorg(request.getParameter("userorg"))
		user.setStatus(1)
		user.setPwd(request.getParameter("pwd"))
		systemUserService.save(user)
		return ServerResultJson.success()
	}
}