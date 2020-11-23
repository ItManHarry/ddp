package com.doosan.ddp.pm.controller.sys.org
import javax.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import com.doosan.ddp.pm.comm.results.ServerResultJson
import com.doosan.ddp.pm.dao.domain.sys.orgs.SystemOrg
import com.doosan.ddp.pm.service.sys.org.SystemOrgService
@Controller
@RequestMapping("/pm/sys/org")
class SystemOrgController {
	
	@Autowired
	SystemOrgService systemOrgService
	
	/**
	 * 	保存系统组织
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
		SystemOrg role = new SystemOrg()
		String id = request.getParameter("id")
		if(id) {
			role = systemOrgService.getOrgById(id);
			role.setModifytime(new Date())
			role.setModifyuserid(userId)
		}else {
			role.setCreatetime(new Date())
			role.setCreateuserid(userId)
		}
		role.setOrgname(request.getParameter("orgname"))
		role.setStatus(1)
		systemOrgService.save(role)
		return ServerResultJson.success()
	}
}
