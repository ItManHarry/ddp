package com.doosan.ddp.pm.controller.sys.test
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
@RequestMapping("/pm/sys/test")
class SystemTestController {
	
	@Autowired
	SystemLogService systemLogService
	@Autowired
	SystemAuthService systemAuthService
	//测试保存日志
	@RequestMapping("/log/save")
	@ResponseBody
	def saveLog(HttpServletRequest request) {
		SystemLog log = new SystemLog()
		def userId = request.getParameter("userid")
		log.setUserid(userId)
		log.setContent(request.getParameter("content"))
		log.setCreatetime(new Date())
		log.setCreateuserid(userId)
		systemLogService.save(log)
		return ServerResultJson.success()
	}
	//测试保存权限
	@RequestMapping("/auth/save")
	@ResponseBody
	def saveAuth(HttpServletRequest request) {
		SystemAuth auth = new SystemAuth()
		def userId = request.getParameter("userid")
		auth.setRoleid(request.getParameter("roleid"))
		auth.setMenuid(request.getParameter("menuid"))
		auth.setCreatetime(new Date())
		auth.setCreateuserid(userId)
		systemAuthService.save(auth)
		return ServerResultJson.success()
	}
	//测试保存权限
	@RequestMapping("/auth/del")
	@ResponseBody
	def deleteAuth(HttpServletRequest request) {
		SystemAuth auth = new SystemAuth()
		systemAuthService.batchDeleteByRoleId(request.getParameter("roleid"))
		SystemLog log = new SystemLog()
		def userId = request.getParameter("userid")
		log.setUserid(userId)
		log.setContent("批量删除授权")
		log.setCreatetime(new Date())
		log.setCreateuserid(userId)
		systemLogService.save(log)
		return ServerResultJson.success()
	}
}