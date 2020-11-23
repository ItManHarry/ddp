package com.doosan.ddp.pm.controller.sys.role
import javax.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import com.doosan.ddp.pm.comm.results.ServerResultJson
import com.doosan.ddp.pm.dao.domain.sys.role.SystemRole
import com.doosan.ddp.pm.service.sys.role.SystemRoleService
@Controller
@RequestMapping("/pm/sys/role")
class SystemRoleController {
	
	@Autowired
	SystemRoleService systemRoleService
	
	/**
	 * 	保存系统角色
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
		SystemRole role = new SystemRole()
		String id = request.getParameter("id")
		if(id) {
			role = systemRoleService.getRoleById(id);
			role.setModifytime(new Date())
			role.setModifyuserid(userId)
		}else {
			role.setCreatetime(new Date())
			role.setCreateuserid(userId)
		}
		role.setRolename(request.getParameter("rolename"))
		role.setStatus(1)
		systemRoleService.save(role)
		return ServerResultJson.success()
	}
}