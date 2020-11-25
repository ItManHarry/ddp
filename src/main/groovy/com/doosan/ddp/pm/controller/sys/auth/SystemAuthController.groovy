package com.doosan.ddp.pm.controller.sys.auth
import java.text.SimpleDateFormat
import javax.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import com.doosan.ddp.pm.comm.results.ServerResultJson
import com.doosan.ddp.pm.dao.domain.sys.auth.SystemAuth
import com.doosan.ddp.pm.dao.domain.sys.log.SystemLog
import com.doosan.ddp.pm.service.sys.auth.SystemAuthService
import com.doosan.ddp.pm.service.sys.log.SystemLogService
@Controller
@RequestMapping("/pm/sys/auth")
class SystemAuthController {
	
	@Autowired
	SystemLogService systemLogService
	@Autowired
	SystemAuthService systemAuthService
	//保存权限
	@RequestMapping("/save")
	@ResponseBody
	def saveAuth(HttpServletRequest request) {
		SystemAuth auth = new SystemAuth()
		def userId = request.getParameter("userid")
		auth.setRoleid(request.getParameter("roleid"))
		auth.setMenuid(request.getParameter("menuid"))
		auth.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
		auth.setCreateuserid(userId)
		systemAuthService.save(auth)
		return ServerResultJson.success()
	}
	//批量删除权限
	@RequestMapping("/del")
	@ResponseBody
	def deleteAuth(HttpServletRequest request) {
		SystemAuth auth = new SystemAuth()
		systemAuthService.batchDeleteByRoleId(request.getParameter("roleid"))
		SystemLog log = new SystemLog()
		def userId = request.getParameter("userid")
		log.setUserid(userId)
		log.setContent("批量删除授权")
		log.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
		log.setCreateuserid(userId)
		systemLogService.save(log)
		return ServerResultJson.success()
	}
}