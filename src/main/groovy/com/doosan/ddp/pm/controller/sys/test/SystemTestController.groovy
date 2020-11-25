package com.doosan.ddp.pm.controller.sys.test
import java.text.SimpleDateFormat
import javax.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import com.doosan.ddp.pm.comm.results.ServerResultJson
import com.doosan.ddp.pm.dao.domain.sys.log.SystemLog
import com.doosan.ddp.pm.service.sys.log.SystemLogService
@Controller
@RequestMapping("/pm/sys/test")
class SystemTestController {
	
	@Autowired
	SystemLogService systemLogService
	//测试保存日志
	@RequestMapping("/log/save")
	@ResponseBody
	def saveLog(HttpServletRequest request) {
		SystemLog log = new SystemLog()
		def userId = request.getParameter("userid")
		log.setUserid(userId)
		log.setContent(request.getParameter("content"))
		log.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
		log.setCreateuserid(userId)
		systemLogService.save(log)
		return ServerResultJson.success()
	}
}